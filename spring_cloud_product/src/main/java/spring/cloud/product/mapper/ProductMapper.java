package spring.cloud.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper {



    @Select("select b.id,b.product_name productName,b.product_num productNum,b.product_price productPrice,b.product_url  productUrl,b.product_start productStart from b_product b")
   List<Map<String,Object>> getAllProduct();
}
