package com.e.dao.redis;

import com.e.support.util.RedisUtil;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * Created by asus on 2017/10/28.
 */
@Repository
public class RedisDS {
    /*
    * 重置过期时间
    * @param key 要重置的键名, timeout设置的过期时间
    * @return 是否重置成功
    * */
    public boolean resetExpireTime(String key,int timeout){
        boolean b = false;
        Jedis jedis = RedisUtil.getJedis();
        long e = jedis.expire(key, timeout);
        if (e==1L){
            b = true;
        }
        return b;
    }

    public String getByKey(String key){
        Jedis jedis = RedisUtil.getJedis();
        return jedis.get(key);
    }
}
