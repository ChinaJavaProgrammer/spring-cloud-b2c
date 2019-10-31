package spring.cloud.product.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import spring.cloud.product.annotation.RedisCount;
import spring.cloud.product.annotation.RequireLogin;
import spring.cloud.product.util.ResponseUtil;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * 
 * @ClassName: AspectConfiguration
 * @Description: TODO 对RedisCount注解进行aop处理防止接口重复提交保证接口的幂等性
 * @author CDLX
 * @date 2019年10月17日
 *
 */
@Aspect
@Configuration
@SuppressWarnings({"unchecked","rawtypes"})
public class AspectConfiguration {
	
	
	@Resource(name="stringTemplate")
	private RedisTemplate redisTemplate;
	
	private static final Log  LOG = LogFactory.getLog(AspectConfiguration.class);

	/**
	 * 
	 * @Title: around
	 * @Description: TODO
	 * @param joinPoint
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException  
	 * @return Object
	 */
	@Around("within(@org.springframework.web.bind.annotation.RestController *) && @annotation(aspect)")
	public Object around(ProceedingJoinPoint joinPoint, RedisCount aspect) throws IllegalAccessException, IllegalArgumentException {
		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		String requstSessionId=request.getSession().getId();
		if(StringUtils.isEmpty(requstSessionId)) {
			LOG.info("获取RequestSessionId");
			requstSessionId=request.getRequestedSessionId();
		}
		//获取执行方法的参数
		Object [] methodParameters=joinPoint.getArgs();
		//获取此注解的作用
		//String kind=joinPoint.getKind();
		//该方法的类
		Object targetClass = joinPoint.getTarget();
		//接口方法签名
		Signature signature = joinPoint.getSignature();
		//用于获取方法
		MethodSignature methodSignature = (MethodSignature)signature;
		//获取当前需要被执行的方法
		Method method=methodSignature.getMethod();
		String methodName=method.getName();
		//以当前请求的sessionId加当前类名加当前方法名称作为redis的key
		String redisKey=requstSessionId+targetClass.getClass().getCanonicalName()+methodName;
		//使用redis的分布式锁如果存入key成功则说明可以访问
		boolean locked=redisTemplate.getConnectionFactory().getConnection().setNX(redisKey.getBytes(),redisKey.getBytes());
		LOG.info("当前RedisKey序列化方式："+redisTemplate.getKeySerializer());
		LOG.info("当前请求传入key是否存在："+locked);
		LOG.info("当前请求传入key："+redisKey);
		//如果当前key存在说明当前方法已经被请求过了则不在执行该方法
		if(locked){
			redisTemplate.expire(redisKey, 20, TimeUnit.SECONDS);
			try {
				Object result = method.invoke(targetClass, methodParameters);
				return result;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}finally {
				redisTemplate.delete(redisKey);
			}
		}else {
			Map<String,Object>map = new HashMap<String, Object>();
			map.put("code", "01");
			map.put("result", "请不要重复提交");
			return map.toString();
		}
		return null;
	}

	@Around("within(@org.springframework.web.bind.annotation.RestController *) && @annotation(aspect)")
	public Object aroud(ProceedingJoinPoint joinPoint, RequireLogin aspect){
		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		HttpSession session = request.getSession();
		if(session.getAttribute("user")==null){
			return ResponseUtil.falseResult(401,"请先登录在做相关操作");
		}
		//获取执行方法的参数
		Object [] methodParameters=joinPoint.getArgs();
		//接口方法签名
		Signature signature = joinPoint.getSignature();
		//用于获取方法
		MethodSignature methodSignature = (MethodSignature)signature;
		//获取当前需要被执行的方法
		Method method=methodSignature.getMethod();
		Object targetClass = joinPoint.getTarget();
		try {
			//该方法的类
			Object result = method.invoke(targetClass, methodParameters);
			return result;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return ResponseUtil.falseResult(500,"服务器错误请联系管理员");
	}
}
