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
package ink.rayin.app.web.oss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Oss类型枚举
 *
 * @author Chill
 */
@Getter
@AllArgsConstructor
public enum OssStatusEnum {

	/**
	 * 关闭
	 */
	DISABLE(1),
	/**
	 * 启用
	 */
	ENABLE(2),
	;

	/**
	 * 类型编号
	 */
	final int num;

}
