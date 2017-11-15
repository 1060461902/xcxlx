package com.e.controller.goods;

import com.e.service.goods.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by asus on 2017/11/1.
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    GoodsService service;
    @RequestMapping(value = "/addorupdate.wx",method = RequestMethod.POST)
    public String addORUpdate(HttpServletRequest request) throws IOException, ServletException {
        String iss = "false";
        if (service.addORUpdate(request)==true){
            iss = "true";
        }
        return iss;
    }
    @RequestMapping(value = "/delete.wx",method = RequestMethod.POST)
    public String delete(HttpServletRequest request) throws IOException {
        String iss = "false";
        if (service.delete(request)==true){
            iss = "true";
        }
        return iss;
    }
    @RequestMapping(value = "/get.wx",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    public String get(HttpServletRequest request) throws IOException {
        return service.getAll(request);
    }
    @RequestMapping(value = "/update.wx",method = RequestMethod.POST)
    public String update(HttpServletRequest request) throws IOException {
        String iss = "false";
        if (service.update(request)==true){
            iss = "true";
        }
        return iss;
    }
}
