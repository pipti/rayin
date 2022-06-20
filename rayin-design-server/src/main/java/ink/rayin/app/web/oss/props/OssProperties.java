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
package ink.rayin.app.web.oss.props;

import ink.rayin.tools.support.Kv;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Minio参数配置类
 *
 * @author Chill
 */
@Data
@ConfigurationProperties(prefix = "oss")
public class OssProperties {

	/**
	 * 是否启用
	 */
	private Boolean enabled;

	/**
	 * 对象存储名称
	 */
	private String name;

	/**
	 * 对象存储服务的URL
	 */
	private String endpoint;

	/**
	 * 应用ID TencentCOS需要
	 */
	private String appId;

	/**
	 * 区域简称 TencentCOS需要
	 */
	private String region;

	/**
	 * Access key就像用户ID，可以唯一标识你的账户
	 */
	private String accessKey;

	/**
	 * Secret key是你账户的密码
	 */
	private String secretKey;

	/**
	 * 默认的存储桶名称
	 */
	private String bucketName = "rayin";

	/**
	 * 自定义属性
	 */
	private Kv args;

}
