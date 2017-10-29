package com.e.controller.user;

import com.e.service.user.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by asus on 2017/10/29.
 */
@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    AddressService service;
    @RequestMapping(value = "/add.wx",method = RequestMethod.POST)
    public String addAddress(HttpServletRequest request) throws IOException {
        String iss = "false";
        if (service.add(request)==true){
            iss = "true";
        }
        return iss;
    }
}
