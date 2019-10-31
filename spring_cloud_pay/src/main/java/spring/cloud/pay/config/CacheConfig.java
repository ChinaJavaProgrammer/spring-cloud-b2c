package spring.cloud.pay.config;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
* 	Redis的缓存同步。
* <p>Title: CacheConfig</p>  
* <p>Description: </p>  
* @author daihu  
* @date 2019年6月11日
 */
@Configuration
@SuppressWarnings({"rawtypes","unchecked"})
public class CacheConfig {
	
	Log log = LogFactory.getLog(this.getClass());
	//需要在配置文件里面配置springext.cache.redis.topic，指定一个频道名字，如果没有配置，默认的频道名字是cache
	@Value("${springext.cache.redis.topic:cache}")
	String topicName;
	
	//参照redis的监听
	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,MessageListenerAdapter listenerAdapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		log.info("topicName:"+topicName);
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic(topicName));
		return container;
	}
	
	//配置一个监听器，MessageListenerAdapter需要实现onMessage方法，我们只需要获取消息内容，这里是指要清空的缓存名字，然后交给MyRedisCacheManager处理即可
	@Bean
	MessageListenerAdapter listenerAdapter(final TwoLevelCacheManager cacheManager) {
		return new MessageListenerAdapter(new MessageListener() {
			
			@Override
			public void onMessage(Message message, byte[] pattern) {
				try {
					
					String cacheName = new String(message.getBody(),"UTF-8");
					RedisTemplate redisTemplate = cacheManager.getRedisTemplate();
					RedisSerializer valueSerializer =redisTemplate.getValueSerializer();
					cacheName=valueSerializer.deserialize(message.getBody()).toString();
					log.info("onMessage=========="+cacheName+",channel:"+new String(message.getChannel()));
					cacheManager.receiver(cacheName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 	构造一个TwoLevelCacheManager较为复杂，这是因为构造RedisCacheManager复杂导致的，构造RedisCacheManager需要如下参数：
	 * RedisCacheWriter，一个实现Redis操作的接口，Spring Boot 提供了NoLock和Lock两种实现，在缓存写操作的时候，前者有较高性能，而后者实现了 Redis的锁
	 * RedisCacheConfiguration，用于设置缓存特性，比如缓存项目的TTL(存活时间)、缓存Key的前缀等，默认情况下TTL为0，不使用前缀。
	 * 	可以为缓存管理器设置默认的配置，也可以为每一个缓存设置一个配置。最为重要的配置是SerializationPair，用于Java和Redis的序列化和反序列化操作
	 * 	这里使用的是自带的JdkSerializationRedisSerializer作为序列化机制，这个类在Redis一章有详细介绍。
	 * @param redisTemplate
	 * @return
	 */
	@Bean
	public TwoLevelCacheManager cacheManager(@Autowired @Qualifier("stringTemplate") RedisTemplate redisTemplate) {
		RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(redisTemplate.getConnectionFactory());
		//SerializationPair用于Java和Redis之间的序列化和反序列化，这里使用JdkSerializationRedisSerializer，并在反序列化过程中，使用当前的ClassLoader
		SerializationPair pair = SerializationPair.fromSerializer(new StringRedisSerializer());
		//构造一个RedisCache的配置，比如是否使用前缀，比如Key和Value的序列化机制
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().serializeKeysWith(pair);
		//自定义的二级缓存容器，实现了redisCacheManager
		TwoLevelCacheManager cacheManager  = new TwoLevelCacheManager(redisTemplate, writer, config);
		
		return cacheManager;
	}
}
