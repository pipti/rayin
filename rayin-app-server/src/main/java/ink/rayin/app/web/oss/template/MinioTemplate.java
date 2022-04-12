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

import ink.rayin.tools.utils.DateUtil;
import ink.rayin.tools.utils.Func;
import ink.rayin.tools.utils.StringPool;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteObject;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ink.rayin.app.web.oss.enums.PolicyType;
import ink.rayin.app.web.oss.model.RayinFile;
import ink.rayin.app.web.oss.model.OssFile;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.app.web.oss.rule.OssRule;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * MinIOTemplate
 *
 * @author Chill
 */
@AllArgsConstructor
public class MinioTemplate implements OssTemplate {

	/**
	 * MinIO客户端
	 */
	private final MinioClient client;

	/**
	 * 存储桶命名规则
	 */
	private final OssRule ossRule;

	/**
	 * 配置类
	 */
	private final OssProperties ossProperties;


	@Override
	@SneakyThrows
	public void makeBucket(String bucketName) {
		if (
			!client.bucketExists(
				BucketExistsArgs.builder().bucket(getBucketName(bucketName)).build()
			)
		) {
			client.makeBucket(
				MakeBucketArgs.builder().bucket(getBucketName(bucketName)).build()
			);
			client.setBucketPolicy(
				SetBucketPolicyArgs.builder().bucket(getBucketName(bucketName)).config(getPolicyType(getBucketName(bucketName), PolicyType.READ)).build()
			);
		}
	}

	@SneakyThrows
	public Bucket getBucket() {
		return getBucket(getBucketName());
	}

	@SneakyThrows
	public Bucket getBucket(String bucketName) {
		Optional<Bucket> bucketOptional = client.listBuckets().stream().filter(bucket -> bucket.name().equals(getBucketName(bucketName))).findFirst();
		return bucketOptional.orElse(null);
	}

	@SneakyThrows
	public List<Bucket> listBuckets() {
		return client.listBuckets();
	}

	@Override
	@SneakyThrows
	public void removeBucket(String bucketName) {
		client.removeBucket(
			RemoveBucketArgs.builder().bucket(getBucketName(bucketName)).build()
		);
	}

	@Override
	@SneakyThrows
	public boolean bucketExists(String bucketName) {
		return client.bucketExists(
			BucketExistsArgs.builder().bucket(getBucketName(bucketName)).build()
		);
	}

	@Override
	@SneakyThrows
	public void copyFile(String bucketName, String fileName, String destBucketName) {
		copyFile(bucketName, fileName, destBucketName, fileName);
	}

	@Override
	@SneakyThrows
	public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
		client.copyObject(
			CopyObjectArgs.builder()
				.source(CopySource.builder().bucket(getBucketName(bucketName)).object(fileName).build())
				.bucket(getBucketName(destBucketName))
				.object(destFileName)
				.build()
		);
	}

	@Override
	@SneakyThrows
	public OssFile statFile(String fileName) {
		return statFile(ossProperties.getBucketName(), fileName);
	}

	@Override
	@SneakyThrows
	public OssFile statFile(String bucketName, String fileName) {
		StatObjectResponse stat = client.statObject(
			StatObjectArgs.builder().bucket(getBucketName(bucketName)).object(fileName).build()
		);
		OssFile ossFile = new OssFile();
		ossFile.setName(Func.isEmpty(stat.object()) ? fileName : stat.object());
		ossFile.setLink(fileLink(ossFile.getName()));
		ossFile.setHash(String.valueOf(stat.hashCode()));
		ossFile.setLength(stat.size());
		ossFile.setPutTime(DateUtil.toDate(stat.lastModified().toLocalDateTime()));
		ossFile.setContentType(stat.contentType());
		return ossFile;
	}

	@Override
	public String filePath(String fileName) {
		return getBucketName().concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	public String filePath(String bucketName, String fileName) {
		return getBucketName(bucketName).concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public String fileLink(String fileName) {
		return ossProperties.getEndpoint().concat(StringPool.SLASH).concat(getBucketName()).concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public String fileLink(String bucketName, String fileName) {
		return ossProperties.getEndpoint().concat(StringPool.SLASH).concat(getBucketName(bucketName)).concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public RayinFile putFile(MultipartFile file) {
		return putFile(ossProperties.getBucketName(), file.getOriginalFilename(), file);
	}

	@Override
	@SneakyThrows
	public RayinFile putFile(String fileName, MultipartFile file) {
		return putFile(ossProperties.getBucketName(), fileName, file);
	}

	@Override
	@SneakyThrows
	public RayinFile putFile(String bucketName, String fileName, MultipartFile file) {
		return putFile(bucketName, file.getOriginalFilename(), file.getInputStream());
	}

	@Override
	@SneakyThrows
	public RayinFile putFile(String fileName, InputStream stream) {
		return putFile(ossProperties.getBucketName(), fileName, stream);
	}

	@Override
	@SneakyThrows
	public RayinFile putFile(String bucketName, String fileName, InputStream stream) {
		return putFile(bucketName, fileName, stream, "application/octet-stream");
	}

	@SneakyThrows
	public RayinFile putFile(String bucketName, String fileName, InputStream stream, String contentType) {
		makeBucket(bucketName);
		String originalName = fileName;
		fileName = getFileName(fileName);
		client.putObject(
			PutObjectArgs.builder()
				.bucket(getBucketName(bucketName))
				.object(fileName)
				.stream(stream, stream.available(), -1)
				.contentType(contentType)
				.build()
		);
		RayinFile file = new RayinFile();
		file.setOriginalName(originalName);
		file.setName(fileName);
		file.setDomain(getOssHost(bucketName));
		file.setLink(fileLink(bucketName, fileName));
		return file;
	}

	@Override
	@SneakyThrows
	public void removeFile(String fileName) {
		removeFile(ossProperties.getBucketName(), fileName);
	}

	@Override
	@SneakyThrows
	public void removeFile(String bucketName, String fileName) {
		client.removeObject(
			RemoveObjectArgs.builder().bucket(getBucketName(bucketName)).object(fileName).build()
		);
	}

	@Override
	@SneakyThrows
	public void removeFiles(List<String> fileNames) {
		removeFiles(ossProperties.getBucketName(), fileNames);
	}

	@Override
	@SneakyThrows
	public void removeFiles(String bucketName, List<String> fileNames) {
		Stream<DeleteObject> stream = fileNames.stream().map(DeleteObject::new);
		client.removeObjects(RemoveObjectsArgs.builder().bucket(getBucketName(bucketName)).objects(stream::iterator).build());
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

	/**
	 * 获取文件外链
	 *
	 * @param bucketName bucket名称
	 * @param fileName   文件名称
	 * @param expires    过期时间 <=7 秒级
	 * @return url
	 */
	@SneakyThrows
	public String getPresignedObjectUrl(String bucketName, String fileName, Integer expires) {
		return client.getPresignedObjectUrl(
			GetPresignedObjectUrlArgs.builder()
				.method(Method.GET)
				.bucket(getBucketName(bucketName))
				.object(fileName)
				.expiry(expires)
				.build()
		);
	}

	/**
	 * 获取存储桶策略
	 *
	 * @param policyType 策略枚举
	 * @return String
	 */
	public String getPolicyType(PolicyType policyType) {
		return getPolicyType(getBucketName(), policyType);
	}

	/**
	 * 获取存储桶策略
	 *
	 * @param bucketName 存储桶名称
	 * @param policyType 策略枚举
	 * @return String
	 */
	public static String getPolicyType(String bucketName, PolicyType policyType) {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		builder.append("    \"Statement\": [\n");
		builder.append("        {\n");
		builder.append("            \"Action\": [\n");

		switch (policyType) {
			case WRITE:
				builder.append("                \"s3:GetBucketLocation\",\n");
				builder.append("                \"s3:ListBucketMultipartUploads\"\n");
				break;
			case READ_WRITE:
				builder.append("                \"s3:GetBucketLocation\",\n");
				builder.append("                \"s3:ListBucket\",\n");
				builder.append("                \"s3:ListBucketMultipartUploads\"\n");
				break;
			default:
				builder.append("                \"s3:GetBucketLocation\"\n");
				break;
		}

		builder.append("            ],\n");
		builder.append("            \"Effect\": \"Allow\",\n");
		builder.append("            \"Principal\": \"*\",\n");
		builder.append("            \"Resource\": \"arn:aws:s3:::");
		builder.append(bucketName);
		builder.append("\"\n");
		builder.append("        },\n");
		if (PolicyType.READ.equals(policyType)) {
			builder.append("        {\n");
			builder.append("            \"Action\": [\n");
			builder.append("                \"s3:ListBucket\"\n");
			builder.append("            ],\n");
			builder.append("            \"Effect\": \"Deny\",\n");
			builder.append("            \"Principal\": \"*\",\n");
			builder.append("            \"Resource\": \"arn:aws:s3:::");
			builder.append(bucketName);
			builder.append("\"\n");
			builder.append("        },\n");

		}
		builder.append("        {\n");
		builder.append("            \"Action\": ");

		switch (policyType) {
			case WRITE:
				builder.append("[\n");
				builder.append("                \"s3:AbortMultipartUpload\",\n");
				builder.append("                \"s3:DeleteObject\",\n");
				builder.append("                \"s3:ListMultipartUploadParts\",\n");
				builder.append("                \"s3:PutObject\"\n");
				builder.append("            ],\n");
				break;
			case READ_WRITE:
				builder.append("[\n");
				builder.append("                \"s3:AbortMultipartUpload\",\n");
				builder.append("                \"s3:DeleteObject\",\n");
				builder.append("                \"s3:GetObject\",\n");
				builder.append("                \"s3:ListMultipartUploadParts\",\n");
				builder.append("                \"s3:PutObject\"\n");
				builder.append("            ],\n");
				break;
			default:
				builder.append("\"s3:GetObject\",\n");
				break;
		}

		builder.append("            \"Effect\": \"Allow\",\n");
		builder.append("            \"Principal\": \"*\",\n");
		builder.append("            \"Resource\": \"arn:aws:s3:::");
		builder.append(bucketName);
		builder.append("/*\"\n");
		builder.append("        }\n");
		builder.append("    ],\n");
		builder.append("    \"Version\": \"2012-10-17\"\n");
		builder.append("}\n");
		return builder.toString();
	}

	/**
	 * 获取域名
	 *
	 * @param bucketName 存储桶名称
	 * @return String
	 */
	public String getOssHost(String bucketName) {
		return ossProperties.getEndpoint() + StringPool.SLASH + getBucketName(bucketName);
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
