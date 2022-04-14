package ink.rayin.app.web.oss.builder;

import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import ink.rayin.app.web.oss.template.OssTemplate;
import ink.rayin.app.web.oss.template.QiniuTemplate;
import lombok.SneakyThrows;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.app.web.oss.rule.StoreRule;

/**
 * 七牛云存储构建类
 *
 * @author Chill
 */
public class QiniuBuilder {

	@SneakyThrows
	public static OssTemplate template(OssProperties ossProperties, StoreRule ossRule) {
		Configuration cfg = new Configuration(Zone.autoZone());
		Auth auth = Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
		UploadManager uploadManager = new UploadManager(cfg);
		BucketManager bucketManager = new BucketManager(auth, cfg);
		return new QiniuTemplate(auth, uploadManager, bucketManager, ossProperties, ossRule);
	}

}
