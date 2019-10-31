package spring.cloud.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import spring.cloud.user.mapper.UserMapper;


@Service
public class MyService {

	private static final Map<String,Object> map = new HashMap<String, Object>();
	Log log =LogFactory.getLog(this.getClass());
	static {
		map.put("key1", "value1");
		map.put("key2", "value2");
		map.put("key3", "value3");
		map.put("key4", "value4");
		map.put("key5", "value5");
	}
	
	@Autowired
	private UserMapper userMapper;
	
	@PostConstruct
	public void init() {
		System.out.println("@PostConstruct");
	}
	
	@PreDestroy
	public void destory() {
		System.out.println("@PreDestroy");
	}
	
	@Cacheable("user")
	public Object get(String Object){
		log.info("进入方法：------------------------------");
		return map.get(Object);
	}
	
	@Cacheable("user2")
	public Object get2(String Object){
		log.info("进入方法：------------------------------");
		return map.get(Object)+"cache2";
	}
	/**
	 * 
	 * @Title: cacheEvict 此注解可以清除缓存名称为 test的缓存中传入参数为Object的缓存
	 * @Description: TODO 清除缓存
	 * @param Object 
	 * @return  
	 * @return String
	 */
	@CacheEvict(value="user",allEntries=true)
	public String cacheEvict(String Object) {
		return "{\"success\":true,\"msg\":\"缓存清空成功\"}";
	}
	
	/**
	 * 
	 * @Title: cacheput
	 * @Description: TODO 更新
	 * @param Object
	 * @return  
	 * @return String
	 */
	@CachePut("user")
	public String cachePut(String Object) {
		map.put(Object, map.get(Object)+"更新之后");
		return map.get(Object).toString();
	}
	
	@Cacheable("list")
	public List<Map<String,Object>>getAllUser(){
		return userMapper.getAll();
	}
	
}
