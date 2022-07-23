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
package ink.rayin.app.web.oss.template;

import com.obs.services.model.ObsObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import ink.rayin.app.web.oss.model.RayinFiles;
import ink.rayin.tools.utils.StringPool;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ink.rayin.app.web.oss.model.RayinFile;
import ink.rayin.app.web.oss.model.StoreFile;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.app.web.oss.rule.StoreRule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 腾讯云 COS 操作
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2020/1/7 17:24
 */
@AllArgsConstructor
public class TencentCosTemplate implements OssTemplate {
	private final COSClient cosClient;
	private final OssProperties ossProperties;
	private final StoreRule ossRule;

	@Override
	@SneakyThrows
	public void makeBucket(String bucketName) {
		if (!bucketExists(bucketName)) {
			cosClient.createBucket(getBucketName(bucketName));
			// TODO: 权限是否需要修改为私有，当前为 公有读、私有写
			cosClient.setBucketAcl(getBucketName(bucketName), CannedAccessControlList.Private);
		}
	}

	@Override
	@SneakyThrows
	public void removeBucket(String bucketName) {
		cosClient.deleteBucket(getBucketName(bucketName));
	}

	@Override
	@SneakyThrows
	public boolean bucketExists(String bucketName) {
		return cosClient.doesBucketExist(getBucketName(bucketName));
	}

	@Override
	@SneakyThrows
	public void copyFile(String bucketName, String fileName, String destBucketName) {
		cosClient.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), fileName);
	}

	@Override
	@SneakyThrows
	public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
		cosClient.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), destFileName);
	}

	@Override
	@SneakyThrows
	public StoreFile statFile(String fileName) {
		return statFile(ossProperties.getBucketName(), fileName);
	}

	@Override
	@SneakyThrows
	public StoreFile statFile(String bucketName, String fileName) {
		ObjectMetadata stat = cosClient.getObjectMetadata(getBucketName(bucketName), fileName);
		StoreFile ossFile = new StoreFile();
		ossFile.setName(fileName);
		ossFile.setLink(fileLink(ossFile.getName()));
		ossFile.setPresignedLink(filePresignedLink(bucketName, fileName));
		ossFile.setHash(stat.getContentMD5());
		ossFile.setLength(stat.getContentLength());
		ossFile.setPutTime(stat.getLastModified());
		ossFile.setContentType(stat.getContentType());
		return ossFile;
	}

	@Override
	@SneakyThrows
	public String filePath(String fileName) {
		return getOssHost().concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public String filePath(String bucketName, String fileName) {
		return getOssHost(bucketName).concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public String fileLink(String fileName) {
		return getOssHost().concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public String fileLink(String bucketName, String fileName) {
		return getOssHost(bucketName + "-" + ossProperties.getAppId()).concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	public String filePresignedLink(String bucketName, String fileName) {
		Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
		return cosClient.generatePresignedUrl(bucketName + "-" + ossProperties.getAppId(),fileName,expiration).toString();
	}

	/**
	 * 文件对象
	 *
	 * @param file 上传文件类
	 * @return
	 */
	@Override
	@SneakyThrows
	public StoreFile putFile(MultipartFile file) {
		return putFile(ossProperties.getBucketName()  + "-" + ossProperties.getAppId(), file.getOriginalFilename(), file);
	}

	/**
	 * @param fileName 上传文件名
	 * @param file     上传文件类
	 * @return
	 */
	@Override
	@SneakyThrows
	public StoreFile putFile(String fileName, MultipartFile file) {
		return putFile(ossProperties.getBucketName(), fileName, file);
	}

	@Override
	@SneakyThrows
	public StoreFile putFile(String bucketName, String fileName, MultipartFile file) {
		return putFile(bucketName, fileName, file.getInputStream());
	}

	@Override
	@SneakyThrows
	public StoreFile putFile(String fileName, InputStream stream) {
		return putFile(ossProperties.getBucketName(), fileName, stream);
	}

	@Override
	@SneakyThrows
	public StoreFile putFile(String bucketName, String fileName, InputStream stream) {
		return put(bucketName, stream, fileName, false);
	}

	@SneakyThrows
	public StoreFile put(String bucketName, InputStream stream, String key, boolean cover) {
		makeBucket(bucketName);
		String originalName = key;
		key = getFileName(key);
		// 覆盖上传
		if (cover) {
			cosClient.putObject(getBucketName(bucketName), key, stream, null);
		} else {
			PutObjectResult response = cosClient.putObject(getBucketName(bucketName), key, stream, null);
			int retry = 0;
			int retryCount = 5;
			while (StringUtils.isBlank(response.getETag()) && retry < retryCount) {
				response = cosClient.putObject(getBucketName(bucketName), key, stream, null);
				retry++;
			}
		}
		StoreFile file = statFile(bucketName, key);
		return file;
	}

	@Override
	@SneakyThrows
	public void removeFile(String fileName) {
		cosClient.deleteObject(getBucketName(), fileName);
	}

	@Override
	@SneakyThrows
	public void removeFile(String bucketName, String fileName) {
		cosClient.deleteObject(getBucketName(bucketName), fileName);
	}

	@Override
	@SneakyThrows
	public void removeFiles(List<String> fileNames) {
		fileNames.forEach(this::removeFile);
	}

	@Override
	@SneakyThrows
	public void removeFiles(String bucketName, List<String> fileNames) {
		fileNames.forEach(fileName -> removeFile(getBucketName(bucketName), fileName));
	}

	@Override
	public RayinFiles getFileList(String bucketName, String keyPrefix) {
		// Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
		//String bucketName = "examplebucket-1250000000";
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
		// 设置bucket名称
		listObjectsRequest.setBucketName(bucketName);
		// prefix表示列出的object的key以prefix开始
		listObjectsRequest.setPrefix(keyPrefix);
		// deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
		listObjectsRequest.setDelimiter("/");
		// 设置最大遍历出多少个对象, 一次listobject最大支持1000
		listObjectsRequest.setMaxKeys(1000);
		ObjectListing objectListing = null;
	//	do {

			objectListing = cosClient.listObjects(listObjectsRequest);

			// common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
			List<String> commonPrefixs = objectListing.getCommonPrefixes();

			// object summary表示所有列出的object列表
			List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
//			for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
//				// 文件的路径key
//				String key = cosObjectSummary.getKey();
//				// 文件的etag
//				String etag = cosObjectSummary.getETag();
//				// 文件的长度
//				long fileSize = cosObjectSummary.getSize();
//				// 文件的存储类型
//				String storageClasses = cosObjectSummary.getStorageClass();
//			}

		//	String nextMarker = objectListing.getNextMarker();
		//	listObjectsRequest.setMarker(nextMarker);
		//} while (objectListing.isTruncated());



		List<RayinFile> files = new ArrayList<>();
		RayinFiles rayinFiles =  new RayinFiles();
		RayinFile rayinFile;
		for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
			rayinFile = new RayinFile();
			rayinFile.setName(cosObjectSummary.getKey().substring(cosObjectSummary.getKey().lastIndexOf(StringPool.SLASH)));
			rayinFile.setPutTime(cosObjectSummary.getLastModified());
			rayinFile.setLength(cosObjectSummary.getSize());
			rayinFile.setPresignedLink(filePresignedLink(bucketName,cosObjectSummary.getKey()));
			rayinFile.setFileType(cosObjectSummary.getStorageClass());
			files.add(rayinFile);
		}
		for (String commonPrefix : objectListing.getCommonPrefixes()) {
			rayinFile = new RayinFile();
			rayinFile.setName(commonPrefix.substring(keyPrefix.length()));
			rayinFile.setFileType("doc");
			rayinFile.setPrefix(keyPrefix);
			files.add(rayinFile);
		}

		rayinFiles.setPrefix(keyPrefix);
		rayinFiles.setFileList(files);
		return rayinFiles;
	}

	/**
	 * 根据规则生成存储桶名称规则
	 *
	 * @return String
	 */
	private String getBucketName() {
		return getBucketName(ossProperties.getBucketName());
	}

	/**
	 * 根据规则生成存储桶名称规则
	 *
	 * @param bucketName 存储桶名称
	 * @return String
	 */
	private String getBucketName(String bucketName) {
		return ossRule.bucketName(bucketName).concat(StringPool.DASH).concat(ossProperties.getAppId());
	}

	/**
	 * 根据规则生成文件名称规则
	 *
	 * @param originalFilename 原始文件名
	 * @return string
	 */
	private String getFileName(String originalFilename) {
		return ossRule.fileName(originalFilename);
	}

	/**
	 * 获取域名
	 *
	 * @param bucketName 存储桶名称
	 * @return String
	 */
	public String getOssHost(String bucketName) {
		return "http://" + cosClient.getClientConfig().getEndpointBuilder().buildGeneralApiEndpoint(getBucketName(bucketName));
	}

	/**
	 * 获取域名
	 *
	 * @return String
	 */
	public String getOssHost() {
		return getOssHost(ossProperties.getBucketName());
	}

}
