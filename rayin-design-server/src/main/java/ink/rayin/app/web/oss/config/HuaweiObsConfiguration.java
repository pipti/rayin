package ink.rayin.app.web.oss.config;

import com.aliyun.oss.OSSClient;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import lombok.AllArgsConstructor;
import ink.rayin.app.web.oss.template.HuaweiObsTemplate;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.app.web.oss.rule.RayinStoreRule;
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
 * @author Tonny
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@AutoConfigureAfter(OssConfiguration.class)
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnClass({OSSClient.class})
@ConditionalOnProperty(value = "oss.name", havingValue = "huaweiobs")
public class HuaweiObsConfiguration {
	private final OssProperties ossProperties;

	@Bean
	@ConditionalOnMissingBean(StoreRule.class)
	public StoreRule ossRule() {
		return new RayinStoreRule();
	}

	@Bean
	@ConditionalOnMissingBean(ObsClient.class)
	public ObsClient ossClient() {
		// 使用可定制各参数的配置类（ObsConfiguration）创建OBS客户端（ObsClient），创建完成后不支持再次修改参数
		ObsConfiguration conf = new ObsConfiguration ();

		conf.setEndPoint(ossProperties.getEndpoint());
		// 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
		conf.setMaxConnections(1024);
		// 设置Socket层传输数据的超时时间，默认为50000毫秒。
		conf.setSocketTimeout(50000);
		// 设置建立连接的超时时间，默认为50000毫秒。
		conf.setConnectionTimeout(50000);
		// 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
		conf.setIdleConnectionTime(60000);
		// 设置失败请求重试次数，默认为3次。
		conf.setMaxErrorRetry(5);

		return new ObsClient(ossProperties.getAccessKey(), ossProperties.getSecretKey(), conf);
	}

	@Bean
	@ConditionalOnMissingBean(HuaweiObsTemplate.class)
	@ConditionalOnBean({ObsClient.class, StoreRule.class})
	public HuaweiObsTemplate huaweiobsTemplate(ObsClient ossClient, StoreRule ossRule) {
		return new HuaweiObsTemplate(ossClient, ossProperties, ossRule);
	}
}
