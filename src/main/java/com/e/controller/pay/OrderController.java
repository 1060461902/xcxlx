package com.e.controller.pay;

import com.e.service.mail.SendOrderMailService;
import com.e.service.pay.OrderService;
import com.e.service.pay.ShowOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by asus on 2017/11/24.
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    ShowOrderService showOrderService;
    @Autowired
    OrderService orderService;
    @Autowired
    SendOrderMailService sendOrderMailService;

    @RequestMapping(value = "/get.wx",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    public String getAll(){
        return showOrderService.getAll();
    }
    @RequestMapping(value = "/status.wx",method = RequestMethod.POST)
    public String updateStatus(HttpServletRequest request) throws IOException {
        String iss = "false";
        if (orderService.updateOrderStatus(request)){
            iss = "true";
        }
        return iss;
    }
    @RequestMapping(value = "/delete.wx",method = RequestMethod.POST)
    public String delete(HttpServletRequest request)throws IOException{
        String iss = "false";
        if (orderService.deleteOrder(request)){
            iss = "true";
        }
        return iss;
    }
    @RequestMapping(value = "/mail.wx",method = RequestMethod.GET)
    public void sendMail(HttpServletRequest request) throws MessagingException {
        String order_id = request.getParameter("order_id");
        sendOrderMailService.sendOrderMail(order_id);
    }
}
