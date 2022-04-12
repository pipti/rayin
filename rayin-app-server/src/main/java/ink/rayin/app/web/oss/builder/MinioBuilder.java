package ink.rayin.app.web.oss.builder;

import ink.rayin.app.web.oss.template.MinioTemplate;
import ink.rayin.app.web.oss.template.OssTemplate;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.app.web.oss.rule.OssRule;

/**
 * Minio云存储构建类
 *
 * @author Chill
 */
public class MinioBuilder {

	@SneakyThrows
	public static OssTemplate template(OssProperties ossProperties, OssRule ossRule) {
		// 创建配置类
//		OssProperties ossProperties = new OssProperties();
//		ossProperties.setEndpoint(oss.getEndpoint());
//		ossProperties.setAccessKey(oss.getAccessKey());
//		ossProperties.setSecretKey(oss.getSecretKey());
//		ossProperties.setBucketName(oss.getBucketName());
		// 创建客户端
		MinioClient minioClient = MinioClient.builder()
				.endpoint(ossProperties.getEndpoint())
				.credentials(ossProperties.getAccessKey(), ossProperties.getSecretKey())
				.build();
		return new MinioTemplate(minioClient, ossRule, ossProperties);
	}

}
