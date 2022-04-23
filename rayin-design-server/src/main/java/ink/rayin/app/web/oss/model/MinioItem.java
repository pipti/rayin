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

import ink.rayin.tools.utils.DateUtil;
import io.minio.messages.Item;
import io.minio.messages.Owner;
import lombok.Data;

import java.util.Date;

/**
 * MinioItem
 *
 * @author Chill
 */
@Data
public class MinioItem {

	private String objectName;
	private Date lastModified;
	private String etag;
	private Long size;
	private String storageClass;
	private Owner owner;
	private boolean isDir;
	private String category;

	public MinioItem(Item item) {
		this.objectName = item.objectName();
		this.lastModified = DateUtil.toDate(item.lastModified().toLocalDateTime());
		this.etag = item.etag();
		this.size = item.size();
		this.storageClass = item.storageClass();
		this.owner = item.owner();
		this.isDir = item.isDir();
		this.category = isDir ? "dir" : "file";
	}

}
