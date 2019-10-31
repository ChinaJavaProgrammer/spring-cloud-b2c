package spring.cloud.user.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.alibaba.druid.pool.DruidDataSource;
/**
 * 
 * @ClassName: DataSourceConfiguration
 * @Description: TODO 阿里连接池数据源设置
 * @author dh
 * @date 2019年10月25日
 *
 */
@Configuration
public class DataSourceConfiguration {

	@Bean("dataSource")
	public DataSource setDataSource(Environment env) {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUsername(env.getProperty("spring.datasource.data-username"));
		dataSource.setPassword(env.getProperty("spring.datasource.data-password"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
		return dataSource;
	}
}
