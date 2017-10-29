package com.e.dao.redis;

import com.e.support.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertTrue;

/**
 * Created by asus on 2017/10/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/config.xml"})
public class RedisDSTest {
    @Autowired
    RedisDS ds;
    @Test
    public void resetExpireTime() throws Exception {
        String key = "test";
        Jedis jedis = RedisUtil.getJedis();
        jedis.expire(key,1);
        jedis.set(key,"t");
        assertTrue(ds.resetExpireTime(key,10));
        System.out.println(jedis.ttl(key));
    }

    @Test
    public void getByKey(){
        System.out.println(ds.getByKey("notEx"));
    }
}