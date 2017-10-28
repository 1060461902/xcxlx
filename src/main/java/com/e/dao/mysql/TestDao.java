package com.e.dao.mysql;

import com.e.model.TestData;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

/**
 * Created by asus on 2017/10/28.
 */
@Repository
@MapperScan
public interface TestDao {
    boolean insert(TestData testData);
}
