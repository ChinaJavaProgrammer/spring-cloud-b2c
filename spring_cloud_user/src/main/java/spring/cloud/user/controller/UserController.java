package spring.cloud.user.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {





    @GetMapping("/login")
    public JSON login(HttpSession session){
        session.setAttribute("user","admin");
        JSONObject json = new JSONObject();

        json.put("success",true);
        json.put("message","登录成功");
        json.put("code",200);
        return json;
    }


}
