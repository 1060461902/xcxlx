package com.e.service;

import com.e.dao.mysql.TestDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by asus on 2017/10/28.
 */
@Service
public class TestService {
    @Resource
    private TestDao dao;

}
