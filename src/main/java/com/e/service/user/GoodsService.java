package com.e.service.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.e.dao.mysql.goods.GoodsDao;
import com.e.model.goods.Goods;
import com.e.support.util.StringFromIsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */
@Service
public class GoodsService {
    @Autowired
    GoodsDao dao;
    /**
     * 单个或批量添加，修改货物信息
     * @param request
     * @return 是否操作成功
     * */
    public boolean addORUpdate(HttpServletRequest request) throws IOException {
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        List<Goods>goodses = new ArrayList<>(JSON.parseArray(json,Goods.class));
        return dao.addORUpdate(goodses);
    }
    /**
     * 单个删除货物信息
     * @param request
     * @return 是否操作成功
     * */
    public boolean delete(HttpServletRequest request) throws IOException {
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        String goods_id = JSON.parseObject(json).getString("goods_id");
        return dao.delete(goods_id);
    }
    /**
     * 获取所有的商品信息
     * @return 商品信息json数组
     * */
    public String getAll(HttpServletRequest request) throws IOException {
        //String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        List<Goods>goodses = dao.getAll();
        return JSON.toJSONString(goodses, SerializerFeature.WriteMapNullValue);
    }
    /**
     * 更新单个货物信息
     * @param request
     * @return 是否操作成功
     * */
    public boolean update(HttpServletRequest request) throws IOException {
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        Goods goods = JSON.parseObject(json,Goods.class);
        return dao.update(goods);
    }
}
