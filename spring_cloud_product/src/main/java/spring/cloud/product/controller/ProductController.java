package spring.cloud.product.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.cloud.product.annotation.RequireLogin;
import spring.cloud.product.service.ProductService;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/productController")
public class ProductController {

    @Autowired
    ProductService productService;


    @GetMapping("/getAllProduct")
    public Object getAllProduct(){
        return productService.getAllProduct();
    }

    @GetMapping("/addToCart")
    @RequireLogin
    public JSON addToCart(HttpSession session, @RequestParam Integer productId){

        return null;
    }
}
