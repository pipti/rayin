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

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import lombok.AllArgsConstructor;
import ink.rayin.app.web.oss.template.AliossTemplate;
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
 * Alioss配置类
 *
 * @author Chill
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@AutoConfigureAfter(OssConfiguration.class)
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnClass({OSSClient.class})
@ConditionalOnProperty(value = "oss.name", havingValue = "aliyunoss")
public class AliyunossConfiguration {

	private final OssProperties ossProperties;
	private final OssRule ossRule;

	@Bean
	@ConditionalOnMissingBean(OSSClient.class)
	public OSSClient ossClient() {
		// 创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
		ClientConfiguration conf = new ClientConfiguration();
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
		CredentialsProvider credentialsProvider = new DefaultCredentialProvider(ossProperties.getAccessKey(), ossProperties.getSecretKey());
		return new OSSClient(ossProperties.getEndpoint(), credentialsProvider, conf);
	}

	@Bean
	@ConditionalOnBean({OSSClient.class})
	@ConditionalOnMissingBean(AliossTemplate.class)
	public AliossTemplate aliossTemplate(OSSClient ossClient) {
		return new AliossTemplate(ossClient, ossProperties, ossRule);
	}

}
