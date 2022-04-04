package ink.rayin.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
@EnableRayinyPdfAdpter
public class RayinSpringbootServerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(RayinSpringbootServerApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(RayinSpringbootServerApplication.class);
	}
}
