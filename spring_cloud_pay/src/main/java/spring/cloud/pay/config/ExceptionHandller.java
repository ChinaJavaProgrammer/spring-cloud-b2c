package spring.cloud.pay.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: ExceptionHandller
 * @Description: TODO 异常全面处理
 * @author dh
 * @date 2019年10月17日
 *
 */
@Controller
public class ExceptionHandller extends AbstractErrorController {

	Log log = LogFactory.getLog(ExceptionHandller.class);
	@Autowired
	ObjectMapper mapper;
	
	public ExceptionHandller(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	@Override
	public String getErrorPath() {
		return null;
	}
	/**
	 * 	替代系统的处理方式
	 * @param modelAndView
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/error")
	public ModelAndView getErrorPath(ModelAndView modelAndView,HttpServletRequest request,HttpServletResponse response) {
		//获取错误信息{timestamp=Sun May 05 20:15:58 CST 2019, status=500, error=Internal Server Error, message=11, path=/testError.json}
		Map<String, Object> errorAttributes=getErrorAttributes(request,false);
		//获取异常
		Throwable error = getCause(request);
		//获取status
		int status = (int)errorAttributes.get("status");
		//获取错误信息
		String message = (String)errorAttributes.get("message");
		//友好提示
		String errorMessage = getErrorMessage(error);
		//后台打印日志信息方便查错
		log.info(status+","+message,error);
		response.setStatus(status);
		if(!isJsonRequest(request)) {
			log.info("request请求");
			//跳转到指定的错误信息页面
			modelAndView.addObject("error", "出错啦！！！");
			if(status==404) {
				modelAndView.setViewName("/404.html");	
			}else {
				modelAndView.setViewName("/error.html");
			}
		}else {
			log.info("json请求");
			Map<String,Object> errorMap = new HashMap<>();
			errorMap.put("success", false);
			errorMap.put("errorMessage", errorMessage);
			errorMap.put("message", message);
			writeJson(response,errorMap);
		}

		return modelAndView;
	}
	
	/**
	 * 用于获取应用系统的异常，定义如下：
	 * @param request
	 * @return
	 */
	protected Throwable getCause(HttpServletRequest request) {
		//从request里面拿到异常对象
		Throwable error = (Throwable)request.getAttribute("javax.servlet.error.exception");
		if(error !=null) {
			//MVC有可能会封装异常为ServletException，需要调用getCause获取真正的异常
			while(error instanceof ServletException && error.getCause()!=null) {
				error=((ServletException)error).getCause();
			}
		}
		return error;
	}
	/**
	 * 	输出报错信息
	 * @param ex
	 * @return
	 */
	protected String getErrorMessage(Throwable ex) {
		return "服务器错误，请联系管理员";
	}
	
	/**
	 * 	区分客户端发起的是页面渲染请求还是JSON请求
	 * @param request
	 * @return
	 */
	protected boolean isJsonRequest(HttpServletRequest request) {
		String requestUri =(String)request.getAttribute("javax.servlet.error.request_uri");
		if(requestUri!=null && requestUri.endsWith(".json")) {
			return true;
		}else if(request.getHeader("Accept").contains("application/json")) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * 	输出json
	 * @param response
	 * @param object
	 */
	protected void writeJson(HttpServletResponse response,Object object) {
		try {
			  response.setHeader("CharSet", "UTF-8");
	            response.setCharacterEncoding("UTF-8");
	            response.setContentType("text/x-json;charset=UTF-8");
	            response.getWriter().print(object);
	            response.getWriter().flush();
	            response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
