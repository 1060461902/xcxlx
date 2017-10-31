package com.e.dao.mysql.goods;

import com.e.model.goods.Goods;

import java.util.List;

/**！！！未完成！！！
 * Created by asus on 2017/10/31.
 * 后台货物管理Dao层
 */
public interface GoodsDao {
    /**
     * 添加货物信息（未确定是否批量）
     * @param
     * @return 是否添加成功
     * */
    boolean add();
    /**
     * 删除货物信息
     * @param goods 货品信息对象
    * @return 是否删除成功
     * */
    boolean delete(Goods goods);
    /**
     * 更新货物信息
     * @param
     *@return 是否更新成功
     * */
    boolean update(Goods goods);
    /**
     * 获取所有货物信息
     * @return 货品信息列表
     * */
    List<Goods> getAll();
}
