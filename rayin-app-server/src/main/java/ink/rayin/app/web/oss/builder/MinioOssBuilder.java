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
public class MinioOssBuilder {

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
