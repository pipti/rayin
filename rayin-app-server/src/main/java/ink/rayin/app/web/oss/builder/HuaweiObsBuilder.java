package ink.rayin.app.web.oss.builder;

import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.app.web.oss.rule.StoreRule;
import ink.rayin.app.web.oss.template.HuaweiObsTemplate;
import lombok.SneakyThrows;

/**
 * 阿里云存储构建类
 *
 * @author Chill
 */
public class HuaweiObsBuilder {

	@SneakyThrows
	public static HuaweiObsTemplate template(OssProperties ossProperties, StoreRule ossRule) {

		ObsConfiguration conf = new ObsConfiguration();
		// 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
		conf.setMaxConnections(1024);
		// 设置Socket层传输数据的超时时间，默认为50000毫秒。
		conf.setSocketTimeout(50000);
		// 设置建立连接的超时时间，默认为50000毫秒。
		conf.setConnectionTimeout(50000);
		// 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
		conf.setConnectionRequestTimeout(1000);
		// 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
		conf.setIdleConnectionTime(60000);
		// 设置失败请求重试次数，默认为3次。
		conf.setMaxErrorRetry(5);
		conf.setEndPoint(ossProperties.getEndpoint());
		// 创建客户端
		ObsClient obsClient = new ObsClient(ossProperties.getAccessKey(),ossProperties.getSecretKey(), conf);
		return new HuaweiObsTemplate(obsClient, ossProperties, ossRule);
	}

}
