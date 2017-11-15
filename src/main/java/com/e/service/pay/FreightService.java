package com.e.service.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.e.dao.mysql.pay.FreightDao;
import com.e.model.pay.Freight;
import com.e.support.util.StringFromIsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by asus on 2017/11/15.
 */
@Service
public class FreightService {
    @Autowired
    FreightDao dao;

    /**
     * 获取所有运费计价规则对象
     * @return json格式字符串 运费计价规则列表
     * */
    public String getAll(){
        List<Freight>freights = dao.getAll();
        return JSON.toJSONString(freights, SerializerFeature.WriteNullStringAsEmpty);
    }
    /**
     * 单个删除运费计价规则
     * @return 是否删除成功
     * */
    public boolean delete(HttpServletRequest request) throws IOException {
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        JSONObject jsonObject = JSON.parseObject(json);
        String freight_id = jsonObject.getString("freight_id");
        return dao.delete(freight_id);
    }
    /**
     * 批量添加，修改运费计价规则
     * @return 是否修改，添加成功
     * */
    public boolean addORUpdate(HttpServletRequest request) throws IOException {
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        List<Freight>freights = JSON.parseArray(json,Freight.class);
        return dao.addORUpdate(freights);
    }
    /**
     * 单个添加运费计价规则
     * @return 是否添加成功
     * */
    public boolean insert(HttpServletRequest request) throws IOException {
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        Freight freight = JSON.parseObject(json,Freight.class);
        return dao.insert(freight);
    }
    /**
     * 单个修改运费计价规则
     * @return 是否修改成功
     * */
    public boolean update(HttpServletRequest request) throws IOException {
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        Freight freight = JSON.parseObject(json,Freight.class);
        return dao.update(freight);
    }
}
