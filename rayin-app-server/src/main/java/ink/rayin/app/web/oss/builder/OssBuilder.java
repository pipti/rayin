/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package ink.rayin.app.web.oss.builder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import ink.rayin.app.web.oss.enums.OssEnum;
import ink.rayin.app.web.oss.rule.OssRule;
import ink.rayin.app.web.oss.rule.RayinOssRule;
import ink.rayin.app.web.oss.template.OssTemplate;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.tools.utils.Func;
import ink.rayin.tools.utils.StringPool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Oss云存储统一构建类
 *
 * @author Chill
 */
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
	 * oss配置缓存池
	 */
	//private final Map<String, Oss> ossPool = new ConcurrentHashMap<>();

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
//		String tenantId = AuthUtil.getTenantId();
//		Oss oss = getOss(tenantId, code);
//		Oss ossCached = ossPool.get(tenantId);
		OssTemplate template = templatePool.get(tenantId);
		// 若为空或者不一致，则重新加载
		if (Func.hasEmpty(template)) {
			synchronized (OssBuilder.class) {
				template = templatePool.get(tenantId);
				if (Func.hasEmpty(template)) {
					OssRule ossRule = new RayinOssRule();
					// 若采用默认设置则开启多租户模式, 若是用户自定义oss则不开启

					if (ossProperties.getName().equals(OssEnum.MINIO.getCategory())) {
						template = MinioOssBuilder.template(ossProperties,ossRule);
					} else if (ossProperties.getName().equals(OssEnum.QINIU.getCategory())) {
						template = QiniuOssBuilder.template(ossProperties,ossRule);
					} else if (ossProperties.getName().equals(OssEnum.ALI.getCategory())) {
						template = AliOssBuilder.template(ossProperties,ossRule);
					} else if (ossProperties.getName().equals(OssEnum.TENCENT.getCategory())) {
						template = TencentOssBuilder.template(ossProperties,ossRule);
					}
					templatePool.put(tenantId, template);
				}
			}
		}
		return template;
	}

	/**
	 * 获取对象存储实体
	 *
	 * @param tenantId 租户ID
	 * @return Oss
	 */
//	public Oss getOss(String tenantId, String code) {
//		String key = tenantId;
//		LambdaQueryWrapper<Oss> lqw = Wrappers.<Oss>query().lambda().eq(Oss::getTenantId, tenantId);
//		// 获取传参的资源编号并查询，若有则返回，若没有则调启用的配置
//		String ossCode = StringUtil.isBlank(code) ? WebUtil.getParameter(OSS_PARAM_KEY) : code;
//		if (StringUtil.isNotBlank(ossCode)) {
//			key = key.concat(StringPool.DASH).concat(ossCode);
//			lqw.eq(Oss::getOssCode, ossCode);
//		} else {
//			lqw.eq(Oss::getStatus, OssStatusEnum.ENABLE.getNum());
//		}
//		Oss oss = CacheUtil.get(RESOURCE_CACHE, OSS_CODE, key, () -> {
//			Oss o = ossService.getOne(lqw);
//			// 若为空则调用默认配置
//			if ((Func.isEmpty(o))) {
//				Oss defaultOss = new Oss();
//				defaultOss.setId(0L);
//				defaultOss.setCategory(OssEnum.of(ossProperties.getName()).getCategory());
//				defaultOss.setEndpoint(ossProperties.getEndpoint());
//				defaultOss.setBucketName(ossProperties.getBucketName());
//				defaultOss.setAccessKey(ossProperties.getAccessKey());
//				defaultOss.setSecretKey(ossProperties.getSecretKey());
//				return defaultOss;
//			} else {
//				return o;
//			}
//		});
//		if (oss == null || oss.getId() == null) {
//			throw new ServiceException("未获取到对应的对象存储配置");
//		} else {
//			return oss;
//		}
//	}

}
