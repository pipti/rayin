package ink.rayin.app.web;


import ink.rayin.springboot.EnableRayinyPdfAdpter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 *
 * [webapp rest api].
 * [WebApp Rest Api]
 * <h3>version info：</h3><br>
 * v1.0 2019年10月15日 Eric Wang创建<br>
 * <br>
 * @author Eric Wang
 * @version 1.0
 * @since JDK 1.8
 */
@Configuration
@SpringBootApplication(exclude={ThymeleafAutoConfiguration.class})
@ComponentScan(basePackages= {"ink.rayin.app.web.**"})
@EnableSwagger2
@MapperScan(basePackages = "ink.rayin.app.web.dao")
@EnableRayinyPdfAdpter
public class RayinRestWebApplication extends SpringBootServletInitializer {
	public static void main(String[] args) throws Exception {
		ApplicationContext applicationContext = SpringApplication.run(RayinRestWebApplication.class, args);
	}

	/**
	 * tomcat启动使用该方法
	 * @param builder
	 * @return
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// 注意这里要指向原先用main方法执行的Application启动类
		return builder.sources(RayinRestWebApplication.class);
	}

//	private CorsConfiguration buildConfig() {
//		CorsConfiguration corsConfiguration = new CorsConfiguration();
//		// 1允许任何域名使用
//		corsConfiguration.addAllowedOrigin("*");
//		// 2允许任何头
//		corsConfiguration.addAllowedHeader("*");
//		// 3允许任何方法（post、get等）
//		corsConfiguration.addAllowedMethod("*");
//		return corsConfiguration;
//	}
//
//	@Bean
//	public CorsFilter corsFilter() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", buildConfig()); // 4
//		return new CorsFilter(source);
//	}

}
