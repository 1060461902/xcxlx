package com.e.dao.redis;

import com.e.support.util.RedisUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;
/**
 * 存储3rd_session
 *
 * @author asus
 * */
@Repository
public class RedisSaveSession {
    /**
* 存储3rd_sessionID 和 openid+session_key到redis中，并设置过期时间
* @param key 3rd_sessionID
* @param value openid+session_key
* @param timeout 过期时间
* @return boolean
* */
    public boolean save(String key,String value,int timeout) throws IOException {
        boolean b = false;
        Jedis jedis = RedisUtil.getJedis();
        jedis.watch(key);
        Transaction multi = jedis.multi();
        multi.set(key, value);
        multi.expire(key,timeout);
        List<Object>resp = multi.exec();
        if (resp.get(0).equals("OK")&&resp.get(1).equals(1L)){
            b = true;
        }else {
            Logger logger = Logger.getLogger(RedisSaveSession.class);
            logger.error("can't save 3rd_session into redis location:RedisSaveSession");
        }
        jedis.unwatch();
        multi.close();
        jedis.close();
        return b;
    }
}
