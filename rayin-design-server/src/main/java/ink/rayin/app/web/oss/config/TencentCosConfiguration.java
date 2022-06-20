package ink.rayin.app.web.oss.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.AllArgsConstructor;
import ink.rayin.app.web.oss.template.TencentCosTemplate;
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
 * <p>
 * 腾讯云 COS 自动装配
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2020/1/7 17:24
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@AutoConfigureAfter(OssConfiguration.class)
@ConditionalOnClass({COSClient.class})
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "oss.name", havingValue = "tencentcos")
public class TencentCosConfiguration {

	private final OssProperties ossProperties;
	private final StoreRule ossRule;


	@Bean
	@ConditionalOnMissingBean(COSClient.class)
	public COSClient ossClient() {
		// 初始化用户身份信息（secretId, secretKey）
		COSCredentials credentials = new BasicCOSCredentials(ossProperties.getAccessKey(), ossProperties.getSecretKey());
		// 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
		Region region = new Region(ossProperties.getRegion());
		// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
		ClientConfig clientConfig = new ClientConfig(region);
		// 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
		clientConfig.setMaxConnectionsCount(1024);
		// 设置Socket层传输数据的超时时间，默认为50000毫秒。
		clientConfig.setSocketTimeout(50000);
		// 设置建立连接的超时时间，默认为50000毫秒。
		clientConfig.setConnectionTimeout(50000);
		// 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
		clientConfig.setConnectionRequestTimeout(1000);
		return new COSClient(credentials, clientConfig);
	}

	@Bean
	@ConditionalOnBean({COSClient.class})
	@ConditionalOnMissingBean(TencentCosTemplate.class)
	public TencentCosTemplate tencentCosTemplate(COSClient cosClient) {
		return new TencentCosTemplate(cosClient, ossProperties, ossRule);
	}

}
