package com.base.cache;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/** Redis Cache Config */
@Configuration("cacheConfig")
@EnableCaching
public class CacheConfig {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private Integer redisPort;

    @Value("${redis.db}")
    private Integer db;

    @Value("${redis.default.ttl}")
    private Integer defaultTtl;

    @Value("${redis.custom.ttl}")
    private Integer customTtl;

    /**
     * Creating bean for JedisConnectionFactory We are also using different DB inside our redis
     *
     * @return
     */
    @Bean
    @Primary
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setDatabase(db);
        return new JedisConnectionFactory(config);
    }

    /**
     * Creating redis template
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    /**
     * defaultCacheManager is for having default TTL which is for longer duration
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    @Primary
    public RedisCacheManager defaultCacheManager(JedisConnectionFactory connectionFactory) {
        return cacheManager(connectionFactory, defaultTtl);
    }

    /**
     * customCacheManager is for having custom TTL which is for shorter duration
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager customCacheManager(RedisConnectionFactory connectionFactory) {
        return cacheManager(connectionFactory, customTtl);
    }

    /**
     * This is generic cache manager which returns the Cache manager based on parameter passed
     *
     * @param connectionFactory
     * @param ttl
     * @return
     */
    private RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, Integer ttl) {
        RedisCacheConfiguration redisCacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .disableCachingNullValues()
                        .entryTtl(Duration.ofHours(ttl))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        redisCacheConfiguration.usePrefix();
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}
