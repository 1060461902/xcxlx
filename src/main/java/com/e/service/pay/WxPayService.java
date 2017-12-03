package com.e.service.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author asus
 * @date 2017/11/6
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
    Logger logger = LoggerFactory.getLogger(WxPayService.class);
    /**
     * 需要前端传的参数：thirdsessionid,goods_array[{goods_id,goods_number}],user_add_message,address_id,express_company_id
     * @return 订单信息对象 order.openid {"error":"lake","goods_id":""}缺货 {"error":"lose"}3rd_session失效
     * */
    @Transactional
    public JSONObject createOrder(HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject result = new JSONObject();
        JSONObject jsonObject = JSON.parseObject(json);
        //获取3rd_sessionID
        String thirdSessionID = jsonObject.getString("thirdsessionid");
        //获取用户附加信息
        String user_add_message = jsonObject.getString("user_add_message");
        //获取地址ID
        String address_id = jsonObject.getString("address_id");
        //获取快递公司ID
        String express_company_id = jsonObject.getString("express_company_id");
        //获取订单货物数组
        JSONArray goodsArray = jsonObject.getJSONArray("goods_array");
        //获取在redis中存储的用户信息
        String sessionValue = redisDS.getByKey(thirdSessionID);
        if (sessionValue!=null&&!sessionValue.equals("")){
            //重置过期时间30天
            boolean b2 = redisDS.resetExpireTime(thirdSessionID,30*60*60*24);
            //如果重置不成功直接抛异常
            if (!b2){
                logger.error("can't reset time:redis");
                throw new RuntimeException("can't reset time:redis");
            }
        }else {
            //如果redis中3rd_sessionID失效,返回lose
            result.put("error","lose");
            return result;
        }
        //获取openid
        String openid = sessionValue.split(",")[1];
        //拼接订单号
        String order_id = openid.substring(0,5)+System.currentTimeMillis();
        UserAddress userAddress = userAddressDao.getTheAddress(address_id,openid);
        if (userAddress==null){
            logger.error("can't get address");
            throw new RuntimeException("can't get address");
        }
        //总重
        double weights = 0;
        int prices = 0;
        List<Order>orders = new ArrayList<>();
        for (int i=0;i<goodsArray.size();i++){
            Order order = new Order();
            //设置订单状态为 0 正在支付(未支付成功)
            order.setOrder_status(0);
            order.setOrder_time(new Timestamp(new java.util.Date().getTime()));
            order.setUser_add_message(user_add_message);
            order.setAddress_id(address_id);
            order.setExpress_company_id(express_company_id);
            order.setOpenid(openid);
            order.setOrder_id(order_id);
            JSONObject goodsJson = goodsArray.getJSONObject(i);
            //获取购买的货物数量
            int goods_number = goodsJson.getInteger("goods_number");
            order.setGoods_number(goods_number);
            //获取货物ID
            String goods_id = goodsJson.getString("goods_id");
            order.setGoods_id(goods_id);
            //根据货物ID获取货物信息
            Goods goods = goodsDao.getGoods(goods_id);
            if (goods==null){
                logger.error("can't get GoodsInfo");
                throw new RuntimeException("can't get GoodsInfo");
            }
            if (goods.getGoods_ava()==0){
                //如果缺货，返回openid为"lake"的订单对象
                result.put("error","lake");
                result.put("goods_id",goods_id);
                return result;
            }
            prices = prices+goods.getGoods_price()*goods_number;
            weights = weights+goods.getGoods_weight()*goods_number;
            orders.add(order);
        }
        String freights = getFreightPrice(userAddress.getAddress(),weights,express_company_id);
        for (int i=0;i<orders.size();i++) {
            Order order = orders.get(i);
            order.setFreight(Integer.parseInt(freights));
            //创建订单
            if (!orderDao.insert(order)) {
                logger.error("can't create order");
                throw new RuntimeException("can't create order");
            }
        }
        result.put("freights",Integer.parseInt(freights));
        result.put("prices",prices);
        result.put("openid",openid);
        result.put("order_id",order_id);
        return result;
    }

    /**
     * 用户对未支付订单重新下单
     * 只需要订单ID
     * */
    @Transactional
    public JSONObject rePay(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject result = new JSONObject();
        JSONObject jsonObject = JSON.parseObject(json);
        String order_id = jsonObject.getString("order_id");
        List<Order>orders = orderDao.getOrderByID(order_id);
        Order order = orders.get(0);
        String openid = order.getOpenid();
        int freights = order.getFreight();
        int prices = 0;
        for (int i=0;i<orders.size();i++){
            Order indexOrder = orders.get(i);
            String goods_id = indexOrder.getGoods_id();
            Goods goods = goodsDao.getGoods(goods_id);
            if (goods.getGoods_ava()==0){
                //如果缺货，返回openid为"lake"的订单对象
                result.put("error","lake");
                result.put("goods_id",goods_id);
                return result;
            }
            int goods_price = goods.getGoods_price();
            int goods_number = indexOrder.getGoods_number();
            prices = prices+goods_number*goods_price;
        }
        result.put("freights",freights);
        result.put("prices",prices);
        result.put("openid",openid);
        result.put("order_id",order_id);
        return result;
    }
    public List<ShowOrder> getShowOrder(String order_id) {
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
        String express_company_id = jsonObject.getString("express_company_id");
        JSONArray goods_array = jsonObject.getJSONArray("goods_array");
        double weights = 0;
        for (int i=0;i<goods_array.size();i++){
            JSONObject goodsJson = goods_array.getJSONObject(i);
            Goods goods = goodsDao.getGoods(goodsJson.getString("goods_id"));
            int goods_num = goodsJson.getInteger("goods_number");
            double weight = goods.getGoods_weight();
            weights = weights+weight*goods_num;
        }
        return getFreightPrice(address,weights,express_company_id);
    }
    /**
     * 获取运费价格 单位 分
     * @param address 地址字符串
     *                要求
     *                普通省：XX省XX市(市必须是地级市)
     *                自治区：XX自治区XX市(市必须是地级市)
     *                直辖市：xxx市
     * @param goods_weight 货物总重
     * @param express_company_id 快递公司ID
     * @return 运费价格字符串格式 单位 分
     * */
    public String getFreightPrice(String address,double goods_weight,String express_company_id) throws IOException {
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
        BigDecimal decimal = calculation(goods_weight,freight,1);
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
        //需要返回的rd
        BigDecimal rd;
        //小于首重按首重价格
        if (weight<=firestWeight){
            rd = new BigDecimal(freight.getFirst_weight().toString());
        }else {
            //首重价格
            BigDecimal firestWeightPrice = new BigDecimal(freight.getFirst_weight().toString());
            //保留一位小数
            firestWeightPrice.setScale(1);
            //续重价格
            BigDecimal continueWeightPrice = new BigDecimal(freight.getContinue_weight().toString());
            //保留一位小数
            continueWeightPrice.setScale(1);
            //扣除首重后的真实重量
            weight = weight - firestWeight;
            String s = Double.toString(weight);
            //续重真实重量
            BigDecimal wgt = new BigDecimal(s);
            //续重真实重量保留一位小数
            wgt.setScale(1,BigDecimal.ROUND_HALF_UP);
            int zs = (int)weight;
            BigDecimal zsbd = new BigDecimal(zs);
            BigDecimal xsbd = wgt.subtract(zsbd);
            BigDecimal half = new BigDecimal("0.5");
            if (xsbd.compareTo(new BigDecimal("0.0"))==0) {
                wgt = zsbd;
            }else if (xsbd.compareTo(half)<=0){
                wgt = zsbd.add(half);
            }else {
                wgt = zsbd.add(half).add(half);
            }
            //续重所需费用
            BigDecimal continuprice = wgt.multiply(continueWeightPrice);
            rd = continuprice.add(firestWeightPrice);
        }
        //保留两位小数，精确到分
        rd.setScale(2);
        return rd;
    }
}
