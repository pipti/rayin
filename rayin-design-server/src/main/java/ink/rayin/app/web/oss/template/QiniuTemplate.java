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

import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import ink.rayin.app.web.oss.model.RayinFiles;
import ink.rayin.tools.utils.CollectionUtil;
import ink.rayin.tools.utils.Func;
import ink.rayin.tools.utils.StringPool;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ink.rayin.app.web.oss.model.RayinFile;
import ink.rayin.app.web.oss.model.StoreFile;
import ink.rayin.app.web.oss.props.OssProperties;
import ink.rayin.app.web.oss.rule.StoreRule;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * QiniuTemplate
 *
 * @author Chill
 */
@AllArgsConstructor
public class QiniuTemplate implements OssTemplate {
	private final Auth auth;
	private final UploadManager uploadManager;
	private final BucketManager bucketManager;
	private final OssProperties ossProperties;
	private final StoreRule ossRule;

	@Override
	@SneakyThrows
	public void makeBucket(String bucketName) {
		if (!CollectionUtil.contains(bucketManager.buckets(), getBucketName(bucketName))) {
			//TODO 默认为公开
			bucketManager.createBucket(getBucketName(bucketName), Zone.autoZone().getRegion());
		}
	}

	@Override
	@SneakyThrows
	public void removeBucket(String bucketName) {

	}

	@Override
	@SneakyThrows
	public boolean bucketExists(String bucketName) {
		return CollectionUtil.contains(bucketManager.buckets(), getBucketName(bucketName));
	}

	@Override
	@SneakyThrows
	public void copyFile(String bucketName, String fileName, String destBucketName) {
		bucketManager.copy(getBucketName(bucketName), fileName, getBucketName(destBucketName), fileName);
	}

	@Override
	@SneakyThrows
	public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
		bucketManager.copy(getBucketName(bucketName), fileName, getBucketName(destBucketName), destFileName);
	}

	@Override
	@SneakyThrows
	public StoreFile statFile(String fileName) {
		return statFile(ossProperties.getBucketName(), fileName);
	}

	@Override
	@SneakyThrows
	public StoreFile statFile(String bucketName, String fileName) {
		FileInfo stat = bucketManager.stat(getBucketName(bucketName), fileName);
		StoreFile ossFile = new StoreFile();
		ossFile.setName(Func.isEmpty(stat.key) ? fileName : stat.key);
		ossFile.setLink(fileLink(bucketName, fileName));
		ossFile.setPresignedLink(filePresignedLink(bucketName, fileName));
		ossFile.setHash(stat.hash);
		ossFile.setLength(stat.fsize);
		ossFile.setPutTime(new Date(stat.putTime / 10000));
		ossFile.setContentType(stat.mimeType);
		return ossFile;
	}

	@Override
	@SneakyThrows
	public String filePath(String fileName) {
		return getBucketName().concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public String filePath(String bucketName, String fileName) {
		return getBucketName(bucketName).concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public String fileLink(String fileName) {
		return ossProperties.getEndpoint().concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public String fileLink(String bucketName, String fileName) {
		return "http://" + bucketManager.domainList(bucketName)[0].concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public String filePresignedLink(String bucketName, String fileName) {
		String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
		String publicUrl = String.format("%s/%s", "http://" + bucketManager.domainList(bucketName)[0], encodedFileName);
		//1小时，可以自定义链接过期时间
		long expireInSeconds = 3600;
		return auth.privateDownloadUrl(publicUrl, expireInSeconds);
	}

	@Override
	@SneakyThrows
	public StoreFile putFile(MultipartFile file) {
		return putFile(ossProperties.getBucketName(), file.getOriginalFilename(), file);
	}

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
			uploadManager.put(stream, key, getUploadToken(bucketName, key), null, null);
		} else {
			Response response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);
			int retry = 0;
			int retryCount = 5;
			while (response.needRetry() && retry < retryCount) {
				response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);
				retry++;
			}
		}
		StoreFile file = statFile(bucketName, key);
		return file;
	}

	@Override
	@SneakyThrows
	public void removeFile(String fileName) {
		bucketManager.delete(getBucketName(), fileName);
	}

	@Override
	@SneakyThrows
	public void removeFile(String bucketName, String fileName) {
		bucketManager.delete(getBucketName(bucketName), fileName);
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
		//每次迭代的长度限制，最大1000，推荐值 1000
		int limit = 1000;
		//指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
		String delimiter = "/";
		//列举空间文件列表
		List<RayinFile> files = new ArrayList<>();
		RayinFiles rayinFiles =  new RayinFiles();
		RayinFile rayinFile;
		BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketName, keyPrefix, limit, delimiter);
		while (fileListIterator.hasNext()) {
			//处理获取的file list结果
			FileInfo[] items = fileListIterator.next();
			if(items.length == 0){
				rayinFile = new RayinFile();
				rayinFile.setName(keyPrefix.substring(keyPrefix.length() - 1));
				rayinFile.setFileType("doc");
				rayinFile.setPrefix(keyPrefix);
				files.add(rayinFile);
			}
			for (FileInfo item : items) {
				System.out.println(item.key);
				System.out.println(item.hash);
				System.out.println(item.fsize);
				System.out.println(item.mimeType);
				System.out.println(item.putTime);
				System.out.println(item.endUser);

				rayinFile = new RayinFile();
				rayinFile.setName(item.key.substring(item.key.lastIndexOf(StringPool.SLASH)));
				rayinFile.setPutTime(new Date(item.putTime));
				rayinFile.setLength(item.fsize);
				rayinFile.setPresignedLink(filePresignedLink(bucketName,item.key));
				rayinFile.setFileType(item.mimeType);
				files.add(rayinFile);
			}

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
	 * 获取上传凭证，普通上传
	 */
	public String getUploadToken(String bucketName) {
		return auth.uploadToken(getBucketName(bucketName));
	}

	/**
	 * 获取上传凭证，覆盖上传
	 */
	private String getUploadToken(String bucketName, String key) {
		return auth.uploadToken(getBucketName(bucketName), key);
	}

	/**
	 * 获取域名
	 *
	 * @return String
	 */
	public String getOssHost() {
		return ossProperties.getEndpoint();
	}

}
