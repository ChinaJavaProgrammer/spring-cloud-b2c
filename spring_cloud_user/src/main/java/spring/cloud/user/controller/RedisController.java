package spring.cloud.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.user.annotation.RedisCount;
import spring.cloud.user.service.MyService;


@RestController
@RequestMapping("redisController")
public class RedisController {

	private static  int a=100000;
	
	@Autowired
	RedisTemplate redisTemplate;
	
	@Autowired
	MyService service;
	
	@RedisCount
	@GetMapping("/test")
	public String test() {
		redisTemplate.opsForValue().set("1", "s");
		return redisTemplate.opsForValue().get("1").toString();
	}
	
	@GetMapping("/test1")
	public String test1() {
		int g=1/0;
		redisTemplate.opsForValue().set("1", "s");
		return redisTemplate.opsForValue().get("1").toString();
	}
	@GetMapping("/decre")
 	public String decre() {
		synchronized(this) {
			a-=1;
		}
		
		return a+"";
	}
	
	@GetMapping("/getA")
	public String getA() {
		return a+"";
	}
	
	@GetMapping("/testCache/{key}")
	public Object testCache(@PathVariable String key) {
		return service.get(key);
	}
	
	@GetMapping("/testCache2/{key}")
	public Object testCache2(@PathVariable String key) {
		return service.get2(key);
	}
	/**
	 * 
	 * @Title: cacheEvict
	 * @Description: TODO 清除缓存
	 * @param key
	 * @return  
	 * @return String
	 */
	@GetMapping("/clearCache/{key}")
	public String cacheEvict(@PathVariable String key) {
		key=null;
		return service.cacheEvict(key);
	}
	
	/**
	 * 
	 * @Title: cachePut
	 * @Description: TODO 缓存更新
	 * @param key
	 * @return  
	 * @return String
	 */
	@GetMapping("/updateCache/{key}")
	public String cachePut(@PathVariable String key) {
		return service.cachePut(key);
	}
	
	@GetMapping("/getAllUser")
	public Object getAllUser() {
		return service.getAllUser();
	}
}
