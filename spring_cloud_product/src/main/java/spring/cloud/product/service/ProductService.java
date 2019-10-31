package spring.cloud.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import spring.cloud.product.mapper.ProductMapper;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {



    @Autowired
    ProductMapper productMapper;

    @Value("${ftp.host}")
    String addr;

    //@Cacheable("product")
    public Object getAllProduct(){
        List<Map<String,Object>>ret = productMapper.getAllProduct();
        ret.forEach( out -> {out.put("productUrl",addr+out.get("productUrl"));
        });
        return ret;
    }
}
