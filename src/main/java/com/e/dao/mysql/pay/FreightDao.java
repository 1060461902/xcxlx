package com.e.dao.mysql.pay;

import com.e.model.pay.Freight;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by asus on 2017/11/15.
 */
@Repository
@MapperScan
public interface FreightDao {
    /**
     * 获取省份，地区相对应的运费计价规则对象
     * @param procince 省份
     * @param place 流向地区
     * @return 省份，地区相对应的运费计价规则对象
     * */
    Freight getFreight(@Param("province")String procince,@Param("place")String place);
    /**
     * 单个添加费计价规则
     * @param freight 运费计价规则对象
     * @return 是否添加成功
     * */
    boolean insert(Freight freight);
    /**
     * 单个修改运费计价规则
     * @param freight 运费计价规则对象
     * @return 是否修改成功
     * */
    boolean update(Freight freight);
    /**
     * 批量增加，修改运费计价规则
     * @param freights 运费计价规则列表
     * @return 是否批量添加，修改成功
     * */
    boolean addORUpdate(List<Freight>freights);
    /**
     * 单个删除运费计价规则
     * @param freight_id 计价规则ID
     * @return 是否删除成功
     * */
    boolean delete(@Param("freight_id") String freight_id);
    /**
     * 获取所有的运费计价规则
     * @return 运费计价规则列表
     * */
    List<Freight>getAll();
}
