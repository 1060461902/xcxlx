package com.e.dao.mysql.user;

import com.e.model.user.UserAddress;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户地址管理
 * Created by asus on 2017/10/29.
 */
@Repository
@MapperScan
public interface UserAddressDao {
    /**
     * 添加地址
     * @param userAddress 用户地址信息对象
     * @return 是否添加成功
     * */
    boolean add(UserAddress userAddress);
    /**
     * 删除地址
     * @param address_id add地址时，后台自动生成的30位随机数，通过前端重新传回后台
     * @param openid 用户在微信应用中的唯一标识，保存在redis中
     * @return 是否删除成功
     * */
    boolean delete(@Param("address_id") String address_id, @Param("openid") String openid);
    /**
     * 更新地址
     * @param userAddress 用户地址信息对象
     * @return 是否更新成功
     * */
    boolean update(UserAddress userAddress);
    /**
     * 前端获取某用户的地址
     * @param openid 用户在微信应用中的唯一标识，保存在redis中
     * @return 用户地址信息对象列表
     * */
    List<UserAddress> get(@Param("openid") String openid);
    /**
     * 根据address_id和openid获取某用户的地址信息
     * @param address_id 地址ID
     * @param openid 用户唯一标识
     * @return 地址信息对象
     * */
    UserAddress getTheAddress(@Param("address_id")String address_id,@Param("openid")String openid);
}
