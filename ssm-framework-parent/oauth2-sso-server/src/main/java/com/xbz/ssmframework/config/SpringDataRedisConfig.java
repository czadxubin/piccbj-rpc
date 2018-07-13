/*package com.xbz.ssmframework.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
//@Order(Ordered.HIGHEST_PRECEDENCE)//OAuth2的资源配置启动序号为0，要保证这里redis提前加载
public class SpringDataRedisConfig {
	@Bean
	@Qualifier("oauth2RedisConnectionFactory")
	public LettuceConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
		config.setPassword(RedisPassword.of("redisPwd"));
		return new LettuceConnectionFactory(config);
	}
	
	@Bean
	@Qualifier("oauth2RedisTemplate")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RedisTemplate redisTemplate() {
		RedisTemplate redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		RedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
		redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
		return redisTemplate;
	}
}
*/