package com.e.service.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e.dao.mysql.goods.GoodsDao;
import com.e.dao.mysql.pay.FreightDao;
import com.e.dao.mysql.pay.OrderDao;
import com.e.dao.mysql.pay.ShowOrderDao;
import com.e.dao.mysql.user.UserAddressDao;
import com.e.dao.redis.RedisDS;
import com.e.model.goods.Goods;
import com.e.model.pay.Freight;
import com.e.model.pay.Order;
import com.e.model.pay.ShowOrder;
import com.e.model.user.UserAddress;
import com.e.service.user.AddressService;
import com.e.support.util.StringFromIsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by asus on 2017/11/6.
 */
@Service
public class WxPayService {
    @Autowired
    FreightDao freightDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    OrderDao orderDao;
    @Autowired
    ShowOrderDao showOrderDao;
    @Autowired
    UserAddressDao userAddressDao;
    @Autowired
    RedisDS redisDS;
    /**
     * 需要前端传的参数：thirdsessionid,goods_id,goods_number,user_add_message,address_id,express_company_id
     * @return 订单信息对象 order.openid "lake"缺货 "lose"3rd_session失效
     * */
    @Transactional
    public Order createOrder(HttpServletRequest request) throws Exception {
        Order order = new Order();
        //设置订单状态为 0 正在支付(未支付成功)
        order.setOrder_status(0);
        order.setOrder_time(new Timestamp(new java.util.Date().getTime()));
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        //获取3rd_sessionID
        String thirdSessionID = jsonObject.getString("thirdsessionid");
        //获取货物ID
        String goods_id = jsonObject.getString("goods_id");
        order.setGoods_id(goods_id);
        //根据货物ID获取货物信息
        Goods goods = goodsDao.getGoods(goods_id);
        if (goods==null){
            throw new Exception("can't get GoodsInfo");
        }
        //获取购买的货物数量
        int goods_number = jsonObject.getInteger("goods_number");
        if (goods.getGoods_num()<goods_number){
            //如果缺货，返回openid为"lake"的订单对象
            order.setOpenid("lake");
            return order;
        }
        //获取用户附加信息
        String user_add_message = jsonObject.getString("user_add_message");
        order.setUser_add_message(user_add_message);
        //获取地址ID
        String address_id = jsonObject.getString("address_id");
        order.setAddress_id(address_id);
        String express_company_id = jsonObject.getString("express_company_id");
        order.setExpress_company_id(express_company_id);
        //获取在redis中存储的用户信息
        String sessionValue = redisDS.getByKey(thirdSessionID);
        if (sessionValue!=null&&!sessionValue.equals("")){
            //重置过期时间30天
            boolean b2 = redisDS.resetExpireTime(thirdSessionID,30*60*60*24);
            //如果重置不成功直接抛异常
            if (!b2){
                Logger logger = LoggerFactory.getLogger(AddressService.class);
                logger.error("can't reset time:redis");
                throw new Exception("can't reset time:redis");
            }
        }else {
            //如果redis中3rd_sessionID失效,返回openid为lose的Order对象
            order.setOpenid("lose");
            return order;
        }
        //获取openid
        String openid = sessionValue.split(",")[1];
        order.setOpenid(openid);
        //拼接商户订单号
        order.setOrder_id(openid.substring(0,5)+System.currentTimeMillis());
        UserAddress userAddress = userAddressDao.getTheAddress(address_id,openid);
        if (userAddress==null){
            throw new Exception("can't get address");
        }
        String freightPrice = getFreightPrice(userAddress.getAddress(),goods_id,express_company_id,goods_number);
        order.setFreight(Integer.parseInt(freightPrice));
        //创建订单
        if (!orderDao.insert(order)){
            throw new Exception("can't create order");
        }
        //更新货物信息，减少货物总量
        if (!goodsDao.updateNum(goods_id, goods_number)) {
            throw new Exception("can't  subtract goods_num");
        }
        return order;
    }

    public ShowOrder getShowOrder(String order_id) {
         return showOrderDao.getTheShowOrder(order_id);
    }

    public boolean updateOrder(String order_id,int status){
        return orderDao.updateTheOrder(order_id,status);
    }

    /**
     *@return 返回运费 单位 分
     * */
    public String returnFreightPrice(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        String address = jsonObject.getString("address");
        String goods_id = jsonObject.getString("goods_id");
        String express_company_id = jsonObject.getString("express_company_id");
        int goods_number = jsonObject.getInteger("goods_number");
        return getFreightPrice(address,goods_id,express_company_id,goods_number);
    }
    /**
     * 获取运费价格 单位 分
     * @param address 地址字符串
     *                要求
     *                普通省：XX省XX市(市必须是地级市)
     *                自治区：XX自治区XX市(市必须是地级市)
     *                直辖市：xxx市
     * @param goods_id 货物ID
     * @param express_company_id 快递公司ID
     * @param goods_number 货物数量
     * @return 运费价格字符串格式 单位 分
     * */
    public String getFreightPrice(String address,String goods_id,String express_company_id,int goods_number) throws IOException {
        int provinceIndex = address.indexOf("省");
        int cityIndex = address.indexOf("市");
        String province;
        String city;
        if (provinceIndex>0){
            //普通省
            province = address.substring(0,provinceIndex);
            city = address.substring(provinceIndex+1,cityIndex);
        }else {
            //自治区
            provinceIndex = address.indexOf("自治区");
            if (provinceIndex>0){
                province = address.substring(0,provinceIndex);
                city = address.substring(provinceIndex+3,cityIndex);
            }else {
                //直辖市
                city = address.substring(0,cityIndex);
                province =city;
            }
        }
        Freight freight = freightDao.getFreight(province,city,express_company_id);
        if(freight==null){
            freight = freightDao.getOtherFreight(province,express_company_id);
            if (freight==null){
                return "unknown";
            }
        }
        Goods goods = goodsDao.getGoods(goods_id);
        BigDecimal decimal = calculation(goods.getGoods_weight()*goods_number,freight,1);
        return decimal.multiply(new BigDecimal("100")).setScale(0).toString();
    }
    /**
     * 计算运价
     * @param weight 重量
     * @param freight 运费计价规则对象
     * @param firestWeight 设置首重重量
     * @return bigDecimal类型的运费 保留两位小数 单位 分
     * */
    public BigDecimal calculation(double weight, Freight freight,double firestWeight){
        BigDecimal rd;//需要返回的rd
        if (weight<=firestWeight){ //小于首重按首重价格
            rd = new BigDecimal(freight.getFirst_weight().toString());
        }else {
            BigDecimal firestWeightPrice = new BigDecimal(freight.getFirst_weight().toString());//首重价格
            firestWeightPrice.setScale(1);//保留一位小数
            BigDecimal continueWeightPrice = new BigDecimal(freight.getContinue_weight().toString());//续重价格
            continueWeightPrice.setScale(1);//保留一位小数
            weight = weight - firestWeight;//扣除首重后的真实重量
            String s = Double.toString(weight);
            BigDecimal wgt = new BigDecimal(s);//续重真实重量
            wgt.setScale(1,BigDecimal.ROUND_HALF_UP);//续重真实重量保留一位小数
            int zs = (int)weight;
            BigDecimal zsbd = new BigDecimal(zs);
            BigDecimal xsbd = wgt.subtract(zsbd);
            BigDecimal half = new BigDecimal("0.5");
            if (xsbd.compareTo(half)<=0){
                wgt = zsbd.add(half);
            }else {
                wgt = zsbd.add(half).add(half);
            }
            BigDecimal continuprice = wgt.multiply(continueWeightPrice); //续重所需费用
            rd = continuprice.add(firestWeightPrice);
        }
        rd.setScale(2);//保留两位小数，精确到分
        return rd;
    }
}
