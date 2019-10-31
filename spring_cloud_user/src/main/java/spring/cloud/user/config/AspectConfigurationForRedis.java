package spring.cloud.user.config;//package com.example.spring.config;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.lang.reflect.Parameter;
//import java.util.Objects;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//@Aspect
//@Configuration
//public class AspectConfigurationForRedis {
//	
//	
//	
//	private static final Log  LOG = LogFactory.getLog(AspectConfigurationForRedis.class);
//
//	@Pointcut(value="@annotation(com.example.spring.annotation.Aop)")
//	public void pointCut() {
//		System.out.println("pointcut");
//	}
//	
//	@Around("pointCut()")
//	public Object around(ProceedingJoinPoint joinPoint) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
//		System.out.println(request.getSession().getId());
//		System.out.println(request.getRequestedSessionId());
//		//获取执行方法的参数
//		Object [] methodParameters=joinPoint.getArgs();
//		System.out.println(joinPoint.getThis());
//		//获取此注解的作用用
//		String kind=joinPoint.getKind();
//		//该方法的类
//		Object targetClass = joinPoint.getTarget();
//		System.out.println(targetClass);
//		//接口方法签名
//		Signature signature = joinPoint.getSignature();
//		//用于获取方法
//		MethodSignature methodSignature = (MethodSignature)signature;
//		Method method=methodSignature.getMethod();
//		Parameter [] parameters=method.getParameters();
//		for (Parameter parameter : parameters) {
//			System.out.println(parameter.getName());
//		}
//		if(false) {
//			return "fgfg";
//		}else {
//			return method.invoke(targetClass, methodParameters);
//		}
//	}
//}
