package com.e.controller.user;

import com.e.service.user.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 * @author asus
 * @date 2017/10/29
 * 用户地址管理界面相关接口
 */
@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    AddressService service;
    @RequestMapping(value = "/add.wx",method = RequestMethod.POST)
    public String addAddress(HttpServletRequest request) throws IOException {
        String ans = service.add(request);
        if (ans.equals("lose")){
            return "lose";
        }else if (ans.equals("suc")){
            return "true";
        }else {
            return "false";
        }
    }
    @RequestMapping(value = "/delete.wx",method = RequestMethod.POST)
    public String deleteAddress(HttpServletRequest request) throws IOException {
        String ans = service.delete(request);
        if (ans.equals("lose")){
            return "lose";
        }else if (ans.equals("suc")){
            return "true";
        }else {
            return "false";
        }
    }
    @RequestMapping(value = "/update.wx",method = RequestMethod.POST)
    public String updateAddress(HttpServletRequest request) throws IOException {
        String ans = service.update(request);
        if (ans.equals("lose")){
            return "lose";
        }else if (ans.equals("suc")){
            return "true";
        }else {
            return "false";
        }
    }
    /**
     * @return 返回地址信息json格式，如果为null则说明3rd_sessionID在后端失效
     * */
    @RequestMapping(value = "/get.wx",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    public String getAddresses(HttpServletRequest request) throws IOException {
        return service.get(request);
    }
}
