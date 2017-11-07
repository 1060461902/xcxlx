package com.e.support.util;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by asus on 2017/11/7.
 * map与object相互转换
 */
public class MOUtil {
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (map==null){
            return null;
        }
        Object object = beanClass.newInstance();
        BeanUtils.populate(object,map);
        return object;
    }

    public static Map<?,?> objectToMap(Object object){
        if (object==null){
            return null;
        }
        return new BeanMap(object);
    }
}
