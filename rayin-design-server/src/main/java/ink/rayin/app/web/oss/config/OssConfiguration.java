package ink.rayin.app.web.oss.config;

import ink.rayin.app.web.oss.builder.OssBuilder;
import lombok.AllArgsConstructor;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.app.web.oss.rule.RayinStoreRule;
import ink.rayin.app.web.oss.rule.StoreRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Oss配置类
 *
 * @author Chill
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@EnableConfigurationProperties(OssProperties.class)
public class OssConfiguration {

	private final OssProperties ossProperties;

	@Bean
	@ConditionalOnMissingBean(StoreRule.class)
	public StoreRule ossRule() {
		return new RayinStoreRule();
	}

	@Bean
	public OssBuilder ossBuilder() {
		return new OssBuilder(ossProperties);
	}
}
