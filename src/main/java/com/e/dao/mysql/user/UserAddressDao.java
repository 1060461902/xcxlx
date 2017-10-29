package com.e.dao.mysql.user;

import com.e.model.user.UserAddress;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

/**
 * Created by asus on 2017/10/29.
 */
@Repository
@MapperScan
public interface UserAddressDao {
    boolean add(UserAddress userAddress);
    boolean delete(String address_id);
    boolean update(UserAddress userAddress);
}
