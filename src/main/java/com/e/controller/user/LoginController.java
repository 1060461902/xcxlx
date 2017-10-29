package com.e.controller.user;

import com.e.service.user.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by asus on 2017/10/12.
 * 登录接口
 */
@RestController
public class LoginController {
    @Autowired
    LoginService loginService;

    @RequestMapping(value = "/login.wx",method = RequestMethod.POST)
    public String login(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        String thirdSessionId = loginService.login(inputStream);
        return thirdSessionId;
    }
}
