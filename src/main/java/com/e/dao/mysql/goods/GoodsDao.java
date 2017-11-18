package com.e.dao.mysql.goods;

import com.e.model.goods.Goods;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;

/**！！！未完成！！！
 * Created by asus on 2017/10/31.
 * 后台货物管理Dao层
 */
@Repository
@MapperScan
public interface GoodsDao {
    /**
     * 添加、更新，货物信息,批量，单个都适用
     * @param
     * @return 是否添加成功
     * */
    boolean addORUpdate(Goods goods);
    /**
     * 删除货物信息
     * @param goods_id 货品ID
    * @return 是否删除成功
     * */
    boolean delete(@Param("goods_id") String goods_id);
    /**
     * 单个更新货物信息
     * @param goods 货物信息对象
     *@return 是否更新成功
     * */
    boolean update(Goods goods);
    /**
     * 获取所有货物信息
     * @return 货品信息列表
     * */
    List<Goods> getAll();
    /**
     * 获取货物图片路径，删除用
     * @param goods_id 货物ID
     * @return 货物图片在服务器中的路径
     * */
    String getRoad(@Param("goods_id") String goods_id);
    /**
     * 获取货物具体信息
     * @param goods_id 货物ID
     * @return 货物信息对象
     * */
    Goods getGoods(@Param("goods_id") String goods_id);
    /**
     *
     * */
}
