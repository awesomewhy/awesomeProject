package com.ozon.online.service.impl.product;

import com.ozon.online.configuration.JedisConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;

@Service
@RequiredArgsConstructor
public class RedisTest {
    private final JedisConfig jedisConfig;


    public void redisTest() {
        JedisPooled jedisPooled = jedisConfig.getJedisPooled();
        jedisPooled.set("qwe", "qwe");

    }
}
