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
package ink.rayin.app.web.oss.config;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import ink.rayin.app.web.oss.template.QiniuTemplate;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.app.web.oss.rule.OssRule;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Qiniu配置类
 *
 * @author Chill
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@AutoConfigureAfter(OssConfiguration.class)
@ConditionalOnClass({Auth.class, UploadManager.class, BucketManager.class})
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "oss.name", havingValue = "qiniu")
public class QiniuConfiguration {

	private final OssProperties ossProperties;
	private final OssRule ossRule;

	@Bean
	@ConditionalOnMissingBean(com.qiniu.storage.Configuration.class)
	public com.qiniu.storage.Configuration qnConfiguration() {
		return new com.qiniu.storage.Configuration(Region.autoRegion());
	}

	@Bean
	@ConditionalOnMissingBean(Auth.class)
	public Auth auth() {
		return Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
	}

	@Bean
	@ConditionalOnBean(com.qiniu.storage.Configuration.class)
	public UploadManager uploadManager(com.qiniu.storage.Configuration cfg) {
		return new UploadManager(cfg);
	}

	@Bean
	@ConditionalOnBean(com.qiniu.storage.Configuration.class)
	public BucketManager bucketManager(com.qiniu.storage.Configuration cfg) {
		return new BucketManager(Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey()), cfg);
	}

	@Bean
	@ConditionalOnBean({Auth.class, UploadManager.class, BucketManager.class})
	@ConditionalOnMissingBean(QiniuTemplate.class)
	public QiniuTemplate qiniuTemplate(Auth auth, UploadManager uploadManager, BucketManager bucketManager) {
		return new QiniuTemplate(auth, uploadManager, bucketManager, ossProperties, ossRule);
	}

}
