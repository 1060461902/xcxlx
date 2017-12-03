package com.e.controller.user;

import com.e.service.user.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author asus
 * @date 2017/10/12
 * 登录接口
 */
@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login.wx",method = RequestMethod.POST)
    public String login(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        String thirdSessionId = loginService.login(inputStream);
        return thirdSessionId;
    }

    @RequestMapping(value = "/keepid.wx",method = RequestMethod.POST)
    public String keepId(HttpServletRequest request){
        String iss = "false";
        try {
            if (loginService.keepId(request)){
                iss = "true";
            }
        } catch (IOException e) {
            logger.error("keep id fail");
            e.printStackTrace();
        }
        return iss;
    }
}
