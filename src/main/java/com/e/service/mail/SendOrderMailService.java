package com.e.service.mail;

import com.e.dao.mysql.pay.ShowOrderDao;
import com.e.model.pay.ShowOrder;
import com.e.service.pay.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by asus on 2017/11/30.
 */
@Service
public class SendOrderMailService {

    @Autowired
    MailService mailService;
    @Autowired
    WxPayService wxPayService;
    @Autowired
    ShowOrderDao showOrderDao;

    @Transactional
    public void sendOrderMail(String order_id) throws MessagingException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dt = format.format(date);
        List<ShowOrder>showOrders = showOrderDao.getTheShowOrder(order_id);
        ShowOrder showOrder = showOrders.get(0);
        String user_name = showOrder.getUser_name();
        String address = showOrder.getAddress();
        String phone = showOrder.getPhone();
        int freights = showOrder.getFreight();
        int prices = 0;
        StringBuffer buffer = new StringBuffer();
        buffer.append("<!DOCTYPE html><html><head><meta charset=\"utf-8\">" +
                "</head><body>");
        buffer.append("<h2>用户姓名</h2>"+"<p>"+user_name+"</p>");
        buffer.append("<h2>用户地址</h2>"+"<p>"+address+"</p>");
        buffer.append("<h2>用户联系方式</h2>"+"<p>"+phone+"</p>");
        for (int i=0;i<showOrders.size();i++){
            ShowOrder indexOrder = showOrders.get(i);
            String goods_id = indexOrder.getGoods_id();
            buffer.append("<h2>"+(i+1)+".商品ID</h2>"+"<p>"+goods_id+"</p>");
            String goods_name = indexOrder.getGoods_name();
            buffer.append("<h2>商品名</h2>"+"<p>"+goods_name+"</p>");
            int goods_price = indexOrder.getGoods_price();
            buffer.append("<h2>商品单价 分</h2>"+"<p>"+goods_price+"</p>");
            int goods_number = indexOrder.getGoods_number();
            buffer.append("<h2>商品购买数量</h2>"+"<p>"+goods_number+"</p>");
            prices = prices+goods_number*goods_price;
        }
        buffer.append("<h2>应收商品总价 分</h2>"+"<p>"+prices+"</p>");
        buffer.append("<h2>应收运费总价 分</h2>"+"<p>"+freights+"</p>");
        buffer.append("<h2>用户支付总额 分</h2>"+"<p>"+(freights+prices)+"</p>");
        buffer.append("</body></html>");
        mailService.send("xlxhost@126.com","订单"+dt,buffer.toString());
    }
}
