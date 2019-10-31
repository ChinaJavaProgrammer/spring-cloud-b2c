package spring.cloud.product.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;


public class TwoLevelCacheManager extends RedisCacheManager {
    private static  int count=1;
	Log log = LogFactory.getLog(this.getClass());
	
	//需要在配置文件里面配置springext.cache.redis.topic，指定一个频道名字，如果没有配置，默认的频道名字是cache
	@Value("${springext.cache.redis.topic:cache}")
	String topicName;
	
	private RedisTemplate redisTemplate;
	
	public TwoLevelCacheManager(RedisTemplate redisTemplate,RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
		super(cacheWriter, defaultCacheConfiguration);
		this.redisTemplate=redisTemplate;
	}
	/**
	 * 返回的是新创建的LocalAndRedisCache
	 */
	@Override
	protected Cache decorateCache(Cache cache) {
		log.info("第"+count+"次创建缓存对象"+cache.getName());
		count++;
		return new RedisAndLocalCache(this,(RedisCache)cache);
	}
	/**
	 * 
	 * @Title: publishMessage
	 * @Description: TODO 消息发布
	 * @param cacheName  
	 * @return void
	 */
	public void publishMessage(String cacheName) {
		this.redisTemplate.convertAndSend(topicName, cacheName);
	}
	/**
	 * 
	 * @Title: receiver
	 * @Description: TODO 消息订阅
	 * @param name  
	 * @return void
	 */
	public void receiver(String name) {
		//从缓存列表中取出名称对应的缓存，allowInFlightCacheCreation默认为true，所以如果没有找到缓存对象则会单独创建一个缓存对象并返回
		RedisAndLocalCache cache = ((RedisAndLocalCache)this.getCache(name));
		if(cache!=null) {
			cache.clearLocal();
		}
	}
	
	public RedisTemplate getRedisTemplate() {
		return this.redisTemplate;
	}
}
