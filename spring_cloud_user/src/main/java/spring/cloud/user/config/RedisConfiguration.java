package spring.cloud.user.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.lettuce.core.models.role.RedisSlaveInstance;
@Configuration
public class RedisConfiguration {

	
	@Bean("stringTemplate")
	public RedisTemplate getRedisTemplate(RedisConnectionFactory connection) {
		RedisTemplate redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connection);
		RedisSerializer<String> redisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(redisSerializer);
		return  redisTemplate;
	}
	
	
//	@Bean
//	public RedisSentinelConfiguration setRedisSentinelConfiguration(Environment env) {
//		RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
//		String [] sentinelArray = env.getProperty("spring.redis.sentinel").split(";");
//		for (String string : sentinelArray) {
//			RedisNode sentinel = new RedisNode(string.substring(0, string.indexOf(":")), Integer.parseInt(string.substring(string.indexOf(":")+1)));
//			configuration.addSentinel(sentinel);
//		}
//		return configuration;
//	}
//	
//	
//	
	
}
