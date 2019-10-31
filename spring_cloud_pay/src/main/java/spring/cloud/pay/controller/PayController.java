package spring.cloud.pay.controller;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.pay.annotation.RedisCount;
import spring.cloud.pay.annotation.RequireLogin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author chengpunan
 * @Description //TODO 支付系统
 * @Date  
 * @Param 
 * @return 
 **/
@RestController
@RequestMapping("/aliPay")
public class PayController {



    //ailiPay网关
    @Value("${gateway}")
    private   String GATEWAY;
    //aliPay私钥
    @Value("${ali_private_key}")
    private   String ALI_PRIVATE_KEY;
    //应用ID
    @Value("${app_id}")
    private   String APP_ID;
    //参数格式
    @Value("${fomart}")
    private   String FOMART;
    //aliPay公钥
    @Value("${ali_public_key}")
    private   String ALI_PUBLIC_KEY;
    //aliPay签名类型
    @Value("${sign_type}")
    private   String sign_type;



    /**
     * @Author chengpunan
     * @Description //TODO
     * @Date  
     * @Param 
     * @return 
     **/
    @GetMapping("/getAliPayPage")
    @RequireLogin
    @RedisCount
    public void aliPay(HttpServletRequest request, HttpServletResponse response,String out_trade_no) {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY, APP_ID, ALI_PRIVATE_KEY, "JSON", "UTF-8", ALI_PUBLIC_KEY, sign_type); //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://114.115.210.183:8080/zzhome/init");
        alipayRequest.setNotifyUrl("http://www.baidu.com");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"201503200101010018858\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":88.88," +
                "    \"subject\":\"测试沙箱支付\"," +
                "    \"body\":\"Iphone6 16G\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            response.setContentType("text/html;charset=" + "UTF-8");
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
