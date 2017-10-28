package com.e.dao.redis;

import com.e.support.util.RedisUtil;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertTrue;

/**
 * Created by asus on 2017/10/28.
 */
public class ExpireTimeTest {
    @Test
    public void resetExpireTime() throws Exception {
        ExpireTime expireTime = new ExpireTime();
        String key = "test";
        Jedis jedis = RedisUtil.getJedis();
        jedis.expire(key,1);
        jedis.set(key,"t");
        assertTrue(expireTime.resetExpireTime(key,10));
        System.out.println(jedis.ttl(key));
    }

}