package com.e.service.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e.dao.mysql.goods.GoodsDao;
import com.e.dao.mysql.pay.FreightDao;
import com.e.model.goods.Goods;
import com.e.model.pay.Freight;
import com.e.model.pay.WxPayOrder;
import com.e.support.util.StringFromIsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by asus on 2017/11/6.
 */
@Service
public class WxPayService {
    @Autowired
    FreightDao freightDao;
    @Autowired
    GoodsDao goodsDao;
    public void createOrder(WxPayOrder order) {
    }

    public WxPayOrder getOrder(String out_trade_no) {
        return null;
    }

    /**
     *
     *
     * */
    public String returnFreightPrice(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        String address = jsonObject.getString("address");
        String goods_id = jsonObject.getString("goods_id");
        String express_company_id = jsonObject.getString("express_company_id");
        return getFreightPrice(address,goods_id,express_company_id);
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
     * @return 运费价格字符串格式 单位 分
     * */
    public String getFreightPrice(String address,String goods_id,String express_company_id) throws IOException {
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
        BigDecimal decimal = calculation(goods.getGoods_weight(),freight,1);
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
        if (weight<=firestWeight){ //小于1kg按首重
            rd = new BigDecimal(freight.getFirst_weight().toString());
        }else {
            BigDecimal firestWeightPrice = new BigDecimal(freight.getFirst_weight().toString());//首重价格
            firestWeightPrice.setScale(1);//保留一位小数
            BigDecimal continueWeightPrice = new BigDecimal(freight.getContinue_weight().toString());//续重价格
            continueWeightPrice.setScale(1);//保留一位小数
            weight = weight - firestWeight;//扣除首重后的重量
            String s = Double.toString(weight);
            BigDecimal wgt = new BigDecimal(s);
            wgt.setScale(1);//重量保留一位小数
            BigDecimal continuprice = wgt.multiply(continueWeightPrice); //续重所需费用
            rd = continuprice.add(firestWeightPrice);
        }
        rd.setScale(2);//保留两位小数，精确到分
        return rd;
    }
}
