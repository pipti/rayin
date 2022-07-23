package ink.rayin.app.web.oss.builder;

import ink.rayin.app.web.exception.BusinessCodeMessage;
import ink.rayin.app.web.exception.RayinBusinessException;
import ink.rayin.app.web.oss.enums.OssEnum;
import ink.rayin.app.web.oss.rule.StoreRule;
import ink.rayin.app.web.oss.rule.RayinStoreRule;
import ink.rayin.app.web.oss.template.OssTemplate;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.htmladapter.base.utils.RayinException;
import ink.rayin.tools.utils.Func;
import ink.rayin.tools.utils.StringPool;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Oss云存储统一构建类
 *
 * @author Chill
 */
@Slf4j
public class OssBuilder {

	public static final String OSS_CODE = "oss:code:";
	public static final String OSS_PARAM_KEY = "code";
	String tenantId = "tenantId";
	private final OssProperties ossProperties;

	public OssBuilder(OssProperties ossProperties) {
		this.ossProperties = ossProperties;
	}

	/**
	 * OssTemplate配置缓存池
	 */
	private final Map<String, OssTemplate> templatePool = new ConcurrentHashMap<>();

	/**
	 * 获取template
	 *
	 * @return OssTemplate
	 */
	public OssTemplate template() {
		return template(StringPool.EMPTY);
	}

	/**
	 * 获取template
	 *
	 * @param code 资源编号
	 * @return OssTemplate
	 */
	public OssTemplate template(String code) {
		OssTemplate template = templatePool.get(tenantId);
		// 若为空或者不一致，则重新加载
		if (Func.hasEmpty(template)) {
			synchronized (OssBuilder.class) {
				template = templatePool.get(tenantId);
				if (Func.hasEmpty(template)) {
					StoreRule ossRule = new RayinStoreRule();
					// 若采用默认设置则开启多租户模式, 若是用户自定义oss则不开启

					if (ossProperties.getName().equals(OssEnum.MINIO.getName())) {
						template = MinioBuilder.template(ossProperties,ossRule);
					} else if (ossProperties.getName().equals(OssEnum.QINIU.getName())) {
						template = QiniuBuilder.template(ossProperties,ossRule);
					} else if (ossProperties.getName().equals(OssEnum.ALIYUN.getName())) {
						template = AliyunOssBuilder.template(ossProperties,ossRule);
					} else if (ossProperties.getName().equals(OssEnum.TENCENT.getName())) {
						template = TencentCosBuilder.template(ossProperties,ossRule);
					} else if (ossProperties.getName().equals(OssEnum.HUAWEI.getName())) {
						template = HuaweiObsBuilder.template(ossProperties,ossRule);
					}else{
						log.warn("对象存储未找到对应的初始化参数，PDF生成存储无法保存！");
						throw RayinBusinessException.buildBizException(BusinessCodeMessage.HTTP_SERVER_INIT_ERROR);
					}
					templatePool.put(tenantId, template);
				}
			}
		}
		return template;
	}

}
