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

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import ink.rayin.app.web.oss.model.RayinFiles;
import ink.rayin.tools.jackson.JsonUtil;
import ink.rayin.tools.utils.StringPool;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ink.rayin.app.web.oss.model.RayinFile;
import ink.rayin.app.web.oss.model.StoreFile;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.app.web.oss.rule.StoreRule;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * AliossTemplate
 *
 * @author Chill
 */
@AllArgsConstructor
public class AliossTemplate implements OssTemplate {
	private final OSSClient ossClient;
	private final OssProperties ossProperties;
	private final StoreRule ossRule;

	@Override
	@SneakyThrows
	public void makeBucket(String bucketName) {
		if (!bucketExists(bucketName)) {
			ossClient.createBucket(getBucketName(bucketName));
		}
	}

	@Override
	@SneakyThrows
	public void removeBucket(String bucketName) {
		ossClient.deleteBucket(getBucketName(bucketName));
	}

	@Override
	@SneakyThrows
	public boolean bucketExists(String bucketName) {
		return ossClient.doesBucketExist(getBucketName(bucketName));
	}

	@Override
	@SneakyThrows
	public void copyFile(String bucketName, String fileName, String destBucketName) {
		ossClient.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), fileName);
	}

	@Override
	@SneakyThrows
	public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
		ossClient.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), destFileName);
	}

	@Override
	@SneakyThrows
	public StoreFile statFile(String fileName) {
		return statFile(ossProperties.getBucketName(), fileName);
	}

	@Override
	@SneakyThrows
	public StoreFile statFile(String bucketName, String fileName) {
		ObjectMetadata stat = ossClient.getObjectMetadata(getBucketName(bucketName), fileName);
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
	public String fileLink(String bucketName, String fileName) {
		return getOssHost(getBucketName(bucketName)).concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public String filePresignedLink(String bucketName, String fileName) {
		Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
		return ossClient.generatePresignedUrl(bucketName,fileName,expiration).toString();
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
		return putFile(ossProperties.getBucketName(), file.getOriginalFilename(), file);

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
			ossClient.putObject(getBucketName(bucketName), key, stream);
		} else {
			PutObjectResult response = ossClient.putObject(getBucketName(bucketName), key, stream);
			int retry = 0;
			int retryCount = 5;
			while (StringUtils.isEmpty(response.getETag()) && retry < retryCount) {
				response = ossClient.putObject(getBucketName(bucketName), key, stream);
				retry++;
			}
		}
		StoreFile file = statFile(bucketName, key);
//		file.setOriginalName(originalName);
//		file.setName(key);
//		file.setDomain(getOssHost(bucketName));
//		file.setLink(fileLink(bucketName, key));
//		file.setPresignedLink(filePresignedLink(bucketName, key));
		return file;
	}

	@Override
	@SneakyThrows
	public void removeFile(String fileName) {
		ossClient.deleteObject(getBucketName(), fileName);
	}

	@Override
	@SneakyThrows
	public void removeFile(String bucketName, String fileName) {
		ossClient.deleteObject(getBucketName(bucketName), fileName);
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
		return ossRule.bucketName(bucketName);
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

	public String getUploadToken() {
		return getUploadToken(ossProperties.getBucketName());
	}

	/**
	 * TODO 过期时间
	 * <p>
	 * 获取上传凭证，普通上传
	 */
	public String getUploadToken(String bucketName) {
		// 默认过期时间2小时
		return getUploadToken(bucketName, ossProperties.getArgs().get("expireTime", 3600L));
	}

	/**
	 * TODO 上传大小限制、基础路径
	 * <p>
	 * 获取上传凭证，普通上传
	 */
	public String getUploadToken(String bucketName, long expireTime) {
		String baseDir = "upload";

		long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
		Date expiration = new Date(expireEndTime);

		PolicyConditions policyConds = new PolicyConditions();
		// 默认大小限制10M
		policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, ossProperties.getArgs().get("contentLengthRange", 10485760));
		policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, baseDir);

		String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
		byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
		String encodedPolicy = BinaryUtil.toBase64String(binaryData);
		String postSignature = ossClient.calculatePostSignature(postPolicy);

		Map<String, String> respMap = new LinkedHashMap<>(16);
		respMap.put("accessid", ossProperties.getAccessKey());
		respMap.put("policy", encodedPolicy);
		respMap.put("signature", postSignature);
		respMap.put("dir", baseDir);
		respMap.put("host", getOssHost(bucketName));
		respMap.put("expire", String.valueOf(expireEndTime / 1000));
		return JsonUtil.toJson(respMap);
	}

	/**
	 * 获取域名
	 *
	 * @param bucketName 存储桶名称
	 * @return String
	 */
	public String getOssHost(String bucketName) {
		String prefix = ossProperties.getEndpoint().contains("https://") ? "https://" : "http://";
		return prefix + getBucketName(bucketName) + StringPool.DOT + ossProperties.getEndpoint().replaceFirst(prefix, StringPool.EMPTY);
	}

	/**
	 * 获取域名
	 *
	 * @return String
	 */
	public String getOssHost() {
		return getOssHost(ossProperties.getBucketName());
	}

	/**
	 * 获取文件列表
	 * @param bucketName
	 * @param keyPrefix
	 * @return
	 */
	@Override
	public RayinFiles getFileList(String bucketName, String keyPrefix){
		// 构造ListObjectsV2Request请求。
		ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request(bucketName);

		// 设置prefix参数来获取fun目录下的所有文件与文件夹。
		listObjectsV2Request.setPrefix(keyPrefix);

		// 设置正斜线（/）为文件夹的分隔符。
		listObjectsV2Request.setDelimiter("/");

		// 发起列举请求。
		ListObjectsV2Result result = ossClient.listObjectsV2(listObjectsV2Request);

		List<OSSObjectSummary> sums = result.getObjectSummaries();
		List<RayinFile> files = new ArrayList<>();
		RayinFiles rayinFiles =  new RayinFiles();
		RayinFile rayinFile;
		for (OSSObjectSummary s : sums) {
			rayinFile = new RayinFile();
			rayinFile.setName(s.getKey().substring(s.getKey().lastIndexOf(StringPool.SLASH)));
			rayinFile.setPutTime(s.getLastModified());
			rayinFile.setLength(s.getSize());
			rayinFile.setPresignedLink(filePresignedLink(bucketName,s.getKey()));
			rayinFile.setFileType(s.getType());
			files.add(rayinFile);
		}
		for (String commonPrefix : result.getCommonPrefixes()) {
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

}
