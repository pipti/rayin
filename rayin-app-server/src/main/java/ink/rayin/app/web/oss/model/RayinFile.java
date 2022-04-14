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
package ink.rayin.app.web.oss.model;

import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

/**
 * BladeFile
 *
 * @author Chill
 */
@Data
public class RayinFile {
	/**
	 * 文件地址
	 */
	private String link;
	/**
	 * 文件地址
	 */
	private String presignedLink;
	/**
	 * 域名地址
	 */
	private String domain;
	/**
	 * 文件名
	 */
	private String name;
	/**
	 * 初始文件名
	 */
	private String originalName;
	/**
	 * 附件表ID
	 */
	private Long attachId;

	/**
	 * 文件上传时间
	 */
	private Date putTime;
	private String putTimeStr;
	/**
	 * 文件大小
	 */
	private long length;
	private String lengthStr;
	/**
	 * 文件大小
	 */
	private String fileType;

	/**
	 * 文件前缀
	 */
	private String prefix;

	public String getPutTimeStr(){
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(putTime != null){
			return dateFormatter.format(putTime);
		}
		return null;
	}

	public String getLengthStr(){
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;

		if (length >= gb) {
			return String.format("%.1f GB", (float) length / gb);
		} else if (length >= mb) {
			float f = (float) length / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		} else if (length >= kb) {
			float f = (float) length / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		} else {
			return String.format("%d B", length);
		}
	}
}
