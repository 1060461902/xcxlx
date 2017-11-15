package com.e.controller.pay;

import com.e.service.pay.FreightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by asus on 2017/11/15.
 */
@RestController
@RequestMapping("/freight")
public class FreightController {
    @Autowired
    FreightService service;

    /**
     * 单个添加运费计价规则
     * @return "true"为成功，"“false"为失败
     * */
    @RequestMapping(value = "/insert.wx",method = RequestMethod.POST)
    public String insert(HttpServletRequest request) throws IOException {
        String iss = "false";
        if (service.insert(request)){
            iss = "true";
        }
        return iss;
    }
    /**
     * 单个修改运费计价规则
     * @return "true"为成功，"“false"为失败
     * */
    @RequestMapping(value = "/update.wx",method = RequestMethod.POST)
    public String update(HttpServletRequest request) throws IOException {
        String iss = "false";
        if (service.update(request)){
            iss = "true";
        }
        return iss;
    }
    /**
     * 单个删除运费计价规则
     * @return "true"为成功，"“false"为失败
     * */
    @RequestMapping(value = "/delete.wx",method = RequestMethod.POST)
    public String delete(HttpServletRequest request) throws IOException {
        String iss = "false";
        if (service.delete(request)){
            iss = "true";
        }
        return iss;
    }
    /**
     * 获取所有运费计价规则
     * @return json格式字符串，运费计价规则列表
     * */
    @RequestMapping(value = "/getAll.wx",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    public String getAll(){
        return service.getAll();
    }
    /**
     * 批量添加或修改运费计价规则
     * @return "true"为成功，"“false"为失败
     * */
    @RequestMapping(value = "/addORUpdate.wx",method = RequestMethod.POST)
    public String addORUpdate(HttpServletRequest request) throws IOException {
        String iss = "false";
        if (service.addORUpdate(request)){
            iss = "true";
        }
        return iss;
    }
}
