package spring.cloud.pay.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 
 * @ClassName: RedisAndLocalCache redis本地缓存(一级缓存)和二级缓存
 * @Description: TODO
 * @author dh
 * @date 2019年10月25日
 *
 */

public class RedisAndLocalCache implements Cache {
	//日志
	Log log = LogFactory.getLog(this.getClass());
	
	//线程安全的hashMap做一级缓存
	ConcurrentHashMap<Object, Object> local = new ConcurrentHashMap<>();
	
	//spring boot提供的redis缓存类
	RedisCache redisCache;
	
	//自定义的二级缓存管理类
	TwoLevelCacheManager cacheManager;
	
	/**
	 * redis本地缓存的构造方法构造redis的缓存类和自定义的redis二级缓存类
	 * @param twoLevelCacheManager
	 * @param cache
	 */
	public RedisAndLocalCache(TwoLevelCacheManager twoLevelCacheManager, RedisCache cache) {
		this.cacheManager=twoLevelCacheManager;
		this.redisCache=cache;
	}

	//获取当前缓存名称
	@Override
	public String getName() {
		return redisCache.getName();
	}

	@Override
	public Object getNativeCache() {
		return redisCache.getNativeCache();
	}

	/**
	 * 	根据Key值来获取缓存对象，ValueWrapper包含了get方法用于获取缓存对象
	 */
	@SuppressWarnings("unused")
	@Override
	public ValueWrapper get(Object key) {
		
		for(Map.Entry<Object, Object> entry : local.entrySet()) {
			System.out.println("key:"+entry.getKey()+",value:"+((ValueWrapper)(entry.getValue())).get());
		}
		//一级缓存先获取
		ValueWrapper wrapper = (ValueWrapper)local.get(key);
		if(wrapper!=null) {
			log.info("当前key："+key+"所对应的缓存在一级缓存中有数据则优先从一级缓存中取值");
			return wrapper;
		}else {
			//获取二级缓存
			wrapper = redisCache.get(key);
			if(wrapper!=null) {
				log.info("当前key："+key+"所对应的缓存在二级缓存中有数据则从二级缓存中取值并将值存入一级缓存");
				Object b = wrapper.get();
				local.put(key, wrapper);
			}else {
				log.info("当前key："+key+"一二级缓存都没有对应的缓存对象");
			}
			return wrapper;
		}
	
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		log.info("get");
		return null;
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		log.info("get1");
		return null;
	}

	/**
	 * 设置缓存
	 */
	@Override
	public void put(Object key, Object value) {
		log.info("将“key为："+key+",value为："+value+"存入缓存中");
		redisCache.put(key, value);
		//通知其他节点缓存更新
		clearOtherJVM();
	}
	/**
	 * 
	 * @Title: clearOtherJVM
	 * @Description: TODO  通过redis的发布和订阅消息来发布缓存名称并删除相关缓存
	 * @return void
	 */
	protected void clearOtherJVM() {
		cacheManager.publishMessage(redisCache.getName());
	}

	/**
	 *	 提供给cacheManager清空一级缓存
	 */
	public void clearLocal() {
		this.local.clear();
	}
	
	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		return null;
	}

	/**
	 * 	根据key清除一二级缓存
	 */
	@Override
	public void evict(Object key) {
			//二级缓存删除
			redisCache.evict(key);
			//一级缓存删除
			this.local.remove(key);
	}

	/**
	 * 	清除所有缓存
	 */
	@Override
	public void clear() {
		redisCache.clear();
	}
}
