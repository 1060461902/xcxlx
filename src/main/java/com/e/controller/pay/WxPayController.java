package com.e.controller.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e.model.pay.MyWxPayConfig;
import com.e.model.pay.ShowOrder;
import com.e.service.pay.ShowOrderService;
import com.e.service.pay.WxPayService;
import com.e.support.util.PayUtil;
import com.e.support.util.StringFromIsUtil;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2017/11/6.
 */
@RestController
@RequestMapping("/wxpay")
public class WxPayController {

    private MyWxPayConfig config;
    private WXPay wxpay;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private ShowOrderService showOrderService;

    public WxPayController() {
        try {
            //初始化微信支付客户端
            config = new MyWxPayConfig();
            wxpay = new WXPay(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 预支付接口
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pay.wx",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    public String pay(HttpServletResponse response,HttpServletRequest request) throws Exception {
        Logger logger = LoggerFactory.getLogger(WxPayController.class);
        //TODO:这里执行商户系统创建新的订单操作
        JSONObject result = wxPayService.createOrder(request);
        String err = result.getString("error");
        if (err!=null){
            logger.error(result.toJSONString());
            return result.toJSONString();
        }
        //设置请求参数
        Map<String, String> data = new HashMap<>();
        data.put("openid",result.getString("openid"));
        data.put("body", "无虾不欢-用户支付");//商品描述
        data.put("out_trade_no", result.getString("order_id"));//商户订单号
        data.put("fee_type", "CNY");//货币类型 CNY人民币
        data.put("total_fee", String.valueOf(result.getInteger("prices")+result.getInteger("freights")));
        //System.out.println(String.valueOf(result.getInteger("prices")+result.getInteger("freights")));
        data.put("spbill_create_ip", request.getRemoteAddr());//用户端ip地址
        //TODO:需要修改回调地址
        data.put("notify_url", "http://127.0.0.1/wx/wxpay/notify_url.wx");//回调接口地址
        data.put("trade_type", "JSAPI");  // 此处指定支付方式 小程序指定为 JSAPI

        try {
            //发起支付
            Map<String, String> resp = wxpay.unifiedOrder(data);
            if (resp.get("return_code").equals("SUCCESS")) {
                String appId = resp.get("appid");
                String prepay_id = resp.get("prepay_id");
                String nonce_str = resp.get("nonce_str");
                String timeStamp = String.valueOf(System.currentTimeMillis());//时间戳要以字符串的形式传给前端
                JSONObject jsonObject = new JSONObject();
                if (resp.get("result_code").equals("SUCCESS")) {
                    jsonObject.put("prepay_id", prepay_id);
                }else {
                    logger.error(resp.get("err_code"));
                    return resp.get("err_code");
                }
                jsonObject.put("nonce_str", nonce_str);
                jsonObject.put("timeStamp", timeStamp);
                jsonObject.put("paySign", PayUtil.getPaySign(appId,nonce_str,prepay_id,timeStamp));
                return jsonObject.toString();
            }else{
                logger.error(resp.get("return_msg"));
                return resp.get("return_msg");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return "false";
    }
    /**
     * 用户重新支付
     * */
    @RequestMapping(value = "/repay.wx",method = RequestMethod.POST)
    public void rePay(HttpServletRequest request){

    }

    /**
     * 支付结果回调
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/notify_url.wx",method = RequestMethod.POST)
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 读取回调内容
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();
        // 支付结果通知的xml格式数据
        String notifyData = sb.toString();
        // 转换成map
        Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);
        //支付确认内容
        String resXml = "";
        //验证签名
        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {        // 签名正确
            List<ShowOrder> orders = wxPayService.getShowOrder(notifyMap.get("out_trade_no"));
            if(orders != null) {
                if("SUCCESS".equals(notifyMap.get("result_code"))) {    //交易成功
                    // TODO:更新订单
                    wxPayService.updateOrder(notifyMap.get("out_trade_no"),1);//将订单状态设置为交易成功
                    //速度过慢，时间过长
                    /*//TODO:发送邮件通知商家
                    ShowOrder showOrder = showOrderService.getTheOrder(notifyMap.get("out_trade_no"));
                    showOrderService.sendObjectEmail(showOrder,ShowOrder.class);*/
                    System.out.println("订单" + notifyMap.get("out_trade_no") + "微信支付成功");
                    //设置成功确认内容
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    //发送通知
                    response.getWriter().println(resXml);
                } else {    //交易失败
                    wxPayService.updateOrder(notifyMap.get("out_trade_no"),4);//将订单状态设置为交易失败
                    //设置失败确认内容
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg></return_msg>" + "</xml> ";
                    System.out.println("订单" + notifyMap.get("out_trade_no") + "微信支付失败");
                    Logger logger = LoggerFactory.getLogger(WxPayController.class);
                    logger.error("交易未成功");
                    //发送通知
                    response.getWriter().println(resXml);
                }
            }
            // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功

        } else {  // 签名错误，如果数据里没有sign字段，也认为是签名错误
            wxPayService.updateOrder(notifyMap.get("out_trade_no"),5);//将订单状态设置为交易失败
            //设置失败确认内容
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg></return_msg>" + "</xml> ";
            Logger logger = LoggerFactory.getLogger(WxPayController.class);
            logger.error("交易未成功 签名错误");
            //发送通知
            PrintWriter out = response.getWriter();
            out.println(resXml);
            out.flush();
            out.close();
        }
    }

    /**
     * 用户确认收货
     * */
    @RequestMapping(value = "/confirm.wx",method = RequestMethod.POST)
    public String confirm(HttpServletRequest request) throws IOException {
        String iss = "false";
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        String order_id = jsonObject.getString("order_id");
        if (wxPayService.updateOrder(order_id,2)){
            iss = "true";
        }
        return iss;
    }
    /**
     * 微信申请退款接口
     * @param out_trade_no 订单号
     * @throws Exception
     */
    @RequestMapping(value = "/refund.wx",method = RequestMethod.POST)
    public void refund(String out_trade_no) throws Exception {
        //设置请求参数
        HashMap<String, String> data = new HashMap<>();
        data.put("out_trade_no", out_trade_no);
        data.put("out_refund_no", out_trade_no);
        data.put("total_fee", "1");
        data.put("refund_fee", "1");
        data.put("refund_fee_type", "CNY");
        data.put("op_user_id", config.getMchID());

        try {
            //调用sdk发起退款
            Map<String, String> result = wxpay.refund(data);
            if("SUCCESS".equals(result.get("result_code"))) {
                //TODO:更新订单

                System.out.println("订单" + out_trade_no + "微信退款成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 运费计算并提供给客户
     *@return 运费字符串 格式 分
     * */
    @RequestMapping(value = "/freight.wx",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    public String getFreight(HttpServletRequest request) throws IOException {
        return wxPayService.returnFreightPrice(request);
    }
    /**
     * @param request 只需要thirdsessionid
     * @return "fail"重置失败 "lose"3rd_sessionID失效 正常：json格式字符串
     * */
    @RequestMapping(value = "/getonepersonall.wx",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    public String  getOnePersonAll(HttpServletRequest request) throws IOException {
        return showOrderService.getOnePersonAll(request);
    }
    /**
     * @param request 只需要order_status
     * @return json格式字符串
     * */
    @RequestMapping(value = "/getthestatusall.wx",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    public String getTheStatusAll(HttpServletRequest request) throws IOException {
        return showOrderService.getTheStatusAll(request);
    }
    /**
     * 获取某人某状态的订单
     * @param request 需要 thirdsessionid,order_status
     * @return "fail"重置失败 "lose"3rd_sessionID失效 正常：json格式字符串
     * */
    @RequestMapping(value = "/getonepersonthestatusall.wx",method = RequestMethod.POST)
    public void getOnePersonTheStatusAll(HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(showOrderService.getOnePersonTheStatusAll(request));
        out.flush();
        out.close();
    }
}
