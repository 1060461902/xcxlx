package com.e.controller.pay;

import com.e.model.pay.WxPayOrder;
import com.e.service.pay.WxPayService;
import com.e.model.pay.MyWxPayConfig;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
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
    @RequestMapping(value = "/pay",method = RequestMethod.POST)
    public void pay(HttpServletResponse response) throws Exception {
        //TODO:这里执行商户系统创建新的订单操作
        WxPayOrder order = new WxPayOrder();
        order.setOut_trade_no(System.currentTimeMillis() + "");
        wxPayService.createOrder(order);

        //设置请求参数
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "微信支付测试");
        data.put("out_trade_no", order.getOut_trade_no());
        data.put("device_info", "");
        data.put("fee_type", "CNY");
        data.put("total_fee", "1");
        data.put("spbill_create_ip", "192.168.0.119");
        data.put("notify_url", "notify_url");
        data.put("trade_type", "NATIVE");  // 此处指定支付方式
        data.put("product_id", "12");

        try {
            //发起支付
            Map<String, String> resp = wxpay.unifiedOrder(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付结果回调
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/notify_url",method = RequestMethod.POST)
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
            WxPayOrder order = wxPayService.getOrder(notifyMap.get("out_trade_no"));
            if(order != null) {
                if("SUCCESS".equals(notifyMap.get("result_code"))) {    //交易成功
                    // TODO:更新订单
                    System.out.println("订单" + notifyMap.get("out_trade_no") + "微信支付成功");
                } else {    //交易失败
                    System.out.println("订单" + notifyMap.get("out_trade_no") + "微信支付失败");
                }
            }
            // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功

            //设置成功确认内容
            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
        }
        else {  // 签名错误，如果数据里没有sign字段，也认为是签名错误
            //设置失败确认内容
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg></return_msg>" + "</xml> ";
            System.out.println("订单" + notifyMap.get("out_trade_no") + "微信支付失败");
        }

        //发送通知
        response.getWriter().println(resXml);
    }

    /**
     * 微信申请退款接口
     * @param out_trade_no        订单号
     * @throws Exception
     */
    @RequestMapping(value = "/refund",method = RequestMethod.POST)
    public void refund(String out_trade_no) throws Exception {
        //设置请求参数
        HashMap<String, String> data = new HashMap<String, String>();
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
}
