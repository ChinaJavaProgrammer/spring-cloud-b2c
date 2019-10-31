package spring.cloud.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
