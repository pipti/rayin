package ink.rayin.app.web.oss.config;

import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ink.rayin.app.web.oss.template.MinioTemplate;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.app.web.oss.rule.StoreRule;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio配置类
 *
 * @author Chill
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@AutoConfigureAfter(OssConfiguration.class)
@ConditionalOnClass({MinioClient.class})
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "oss.name", havingValue = "minio")
public class MinioConfiguration {

	private final OssProperties ossProperties;
	private final StoreRule ossRule;


	@Bean
	@SneakyThrows
	@ConditionalOnMissingBean(MinioClient.class)
	public MinioClient minioClient() {
		return MinioClient.builder()
			.endpoint(ossProperties.getEndpoint())
			.credentials(ossProperties.getAccessKey(), ossProperties.getSecretKey())
			.build();
	}

	@Bean
	@ConditionalOnBean({MinioClient.class})
	@ConditionalOnMissingBean(MinioTemplate.class)
	public MinioTemplate minioTemplate(MinioClient minioClient) {
		return new MinioTemplate(minioClient, ossRule, ossProperties);
	}

}
