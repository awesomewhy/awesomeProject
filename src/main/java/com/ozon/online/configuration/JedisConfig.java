package com.ozon.online.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPooled;

@Configuration
public class JedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private Integer port;

    @Bean
    public JedisPooled getJedisPooled() {
        HostAndPort hostAndPort = new HostAndPort(host, port);
        JedisPooled jedisWithTimeout = new JedisPooled(hostAndPort,
                DefaultJedisClientConfig.builder()
                        .socketTimeoutMillis(5000)
                        .connectionTimeoutMillis(5000)
                        .build());
        return jedisWithTimeout;
    }
}
