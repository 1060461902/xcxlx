package com.e.controller;

import com.e.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by asus on 2017/10/12.
 */
@RestController
public class LoginController {
    @Autowired
    LoginService loginService;

    @RequestMapping(value = "/login.wx",method = RequestMethod.POST)
    public String test(HttpServletRequest request, HttpServletResponse response) throws IOException {
        InputStream inputStream = request.getInputStream();
        String thirdSessionId = loginService.login(inputStream);
        return thirdSessionId;
    }
}
