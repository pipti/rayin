package ink.rayin.app.web.oss.config;

import ink.rayin.app.web.oss.builder.OssBuilder;
import lombok.AllArgsConstructor;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.app.web.oss.rule.RayinOssRule;
import ink.rayin.app.web.oss.rule.OssRule;
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
	@ConditionalOnMissingBean(OssRule.class)
	public OssRule ossRule() {
		return new RayinOssRule();
	}

	@Bean
	public OssBuilder ossBuilder() {
		return new OssBuilder(ossProperties);
	}
}
