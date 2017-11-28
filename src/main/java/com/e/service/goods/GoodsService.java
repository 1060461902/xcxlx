package com.e.service.goods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.e.dao.mysql.goods.GoodsDao;
import com.e.model.goods.Goods;
import com.e.support.util.StringFromIsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

/**
 * Created by asus on 2017/11/1.
 */
@Service
public class GoodsService {
    @Autowired
    GoodsDao dao;
    /**
     * 单个添加或修改货物信息
     * @param request
     * @return 是否操作成功
     * */
    public boolean addORUpdate(HttpServletRequest request) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getSession().getServletContext().getRealPath("images");
        Part part = request.getPart("file");
        String header = part.getHeader("content-disposition");
        String fileName = getFileName(header);
        try {
            part.write(path + File.separator + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        InetAddress address = InetAddress.getLocalHost();//获取的是本地的IP地址 //PC-20140317PXKX/192.168.0.121
        String hostAddress = address.getHostAddress();
        String goods_id = request.getParameter("goods_id");
        String goods_name = request.getParameter("goods_name");
        String goods_price = request.getParameter("goods_price");
        String goods_num = request.getParameter("goods_num");
        String goods_weight = request.getParameter("goods_weight");
        String goods_img = hostAddress+request.getContextPath()+"/images/"+fileName;
        Goods goods = new Goods();
        goods.setGoods_img(goods_img);
        goods.setGoods_price(Integer.parseInt(goods_price));
        goods.setGoods_name(goods_name);
        goods.setGoods_num(Integer.parseInt(goods_num));
        goods.setGoods_id(goods_id);
        goods.setGoods_weight(Double.parseDouble(goods_weight));
        return dao.addORUpdate(goods);
    }
    /**
     * 获取文件名
     * @param header
     * @return 文件名
     * */
    private String getFileName(String header) {
        String[] tempArr1 = header.split(";");
        String[] tempArr2 = tempArr1[2].split("=");
        String fileName = tempArr2[1].substring(tempArr2[1].lastIndexOf("\\") + 1).replaceAll("\"", "");
        return fileName;
    }
    /**
     * 单个删除货物信息
     * @param request
     * @return 是否操作成功
     * */
    public boolean delete(HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        String goods_id = JSON.parseObject(json).getString("goods_id");
        String road = dao.getRoad(goods_id);
        File file = new File(road);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                return false;
            }
        }
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
        request.setCharacterEncoding("UTF-8");
        String json = StringFromIsUtil.getData(request.getInputStream(),"UTF-8");
        Goods goods = JSON.parseObject(json,Goods.class);
        return dao.update(goods);
    }
}
