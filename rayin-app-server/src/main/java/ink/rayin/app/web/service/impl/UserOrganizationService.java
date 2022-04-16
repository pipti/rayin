
package ink.rayin.app.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import ink.rayin.app.web.exception.RayinBusinessException;
import ink.rayin.app.web.model.Message;
import ink.rayin.app.web.model.Organization;
import ink.rayin.app.web.model.UserOrganization;
import ink.rayin.app.web.model.Users;
import ink.rayin.app.web.dao.*;
import ink.rayin.app.web.oss.builder.OssBuilder;
import ink.rayin.app.web.service.IUserOrganizationService;

import ink.rayin.tools.utils.ResourceUtil;
import ink.rayin.tools.utils.StringUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * [用户项目管理服务].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-02-15 Wang Zhu create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
@Service
@Slf4j
public class UserOrganizationService implements IUserOrganizationService {

	@Resource
	private UserOrganizationMapper userOrganizationMapper;
	@Resource
	private UsersMapper usersMapper;
	@Resource
	private OrganizationMapper organizationMapper;
	@Resource
	private UserElementMapper userElementMapper;
	@Resource
	private UserElementVersionHistoryMapper userElementVersionHistoryMapper;
	@Resource
	private UserTemplateElementMapper userTemplateElementMapper;
	@Resource
	private UserTemplateMapper userTemplateMapper;
	/**
	 * 对象存储构建类
	 */
	@Resource
	private OssBuilder ossBuilder;
	@Value("${rayin.file.folder}")
	private String folder;

	/**
	 * 项目分页查询
	 *
	 * @param page
	 * @param page
	 * @return
	 */
	@Override
	public IPage<UserOrganization> userOrganizationQuery(IPage<UserOrganization> page, UserOrganization uo) {
		return userOrganizationMapper.userOrganizationQuery(page, uo);
	}

	/**
	 * 项目分页查询
	 *
	 * @param uo
	 * @return
	 */
	@Override
	public UserOrganization userOrganizationQueryOne(UserOrganization uo) {
		return userOrganizationMapper.userOrganizationQueryOne(uo);
	}


	/**
	 * 项目成员分页查询
	 *
	 * @param page
	 * @param page
	 * @return
	 */
	@Override
	public IPage<UserOrganization> userMemberQuery(IPage<UserOrganization> page, UserOrganization uo) {
		return userOrganizationMapper.userMemberQuery(page, uo);
	}

	/**
	 * 待添加项目成员查询
	 *
	 * @param page
	 * @param page
	 * @return
	 */
	@Override
	public IPage<Users> userAddMemberQuery(IPage<Users> page, Users uo) {
		QueryWrapper<Users> qw = new QueryWrapper<Users>();
		qw.eq("username", uo.getUsername());
		return usersMapper.selectPage(page, qw);
	}


	/**
	 * 项目成员添加
	 *
	 * @param uo
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userOrganizationMemberAdd(UserOrganization uo) {
		QueryWrapper<UserOrganization> qw = new QueryWrapper<UserOrganization>();
		qw.eq("user_id", uo.getUserId());
		qw.eq("organization_id", uo.getOrganizationId());
		if (userOrganizationMapper.selectList(qw).size() > 0) {
			throw new RayinBusinessException("该用户在该项目已存在，无需再添加。");
		}
		//默认的角色为2
		uo.setRoleId(2);
		//TODO 通知被添加用户
		Organization organization = organizationMapper.selectById(uo.getOrganizationId());
		Message message = new Message();
		message.setUserId(uo.getUserId());
		message.setInfo("您成为'" + organization.getOrganizationName() + "'项目成员");
		message.setUrl("test");
		message.setChecked(false);
		return userOrganizationMapper.insert(uo);
	}

	/**
	 * 项目成员删除
	 *
	 * @param uo
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userOrganizationMemberDel(UserOrganization uo, String userId, String orgId) {

		QueryWrapper<UserOrganization> qw1 = new QueryWrapper<UserOrganization>();
		qw1.eq("user_id", userId);
		qw1.eq("organization_id", orgId);
		qw1.eq("owner", true);
		if (userOrganizationMapper.selectOne(qw1) == null) {
			throw new RayinBusinessException("您无权操作该项目的成员设置，请联系该项目创建人！");
		}


		UpdateWrapper<UserOrganization> qw2 = new UpdateWrapper<UserOrganization>();
		qw2.eq("user_id", uo.getUserId()).eq("organization_id", uo.getOrganizationId());
		//TODO 通知被删除用户
		Organization organization = organizationMapper.selectById(uo.getOrganizationId());
		Message message = new Message();
		message.setUserId(uo.getUserId());
		message.setInfo("您已被移出'" + organization.getOrganizationName() + "'项目");
		message.setUrl("test");
		message.setChecked(false);
		return userOrganizationMapper.delete(qw2);
	}

	/**
	 * 项目权利移交
	 *
	 * @param uo
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userOrganizationRightTrans(UserOrganization uo, String userId, String orgId) {

		QueryWrapper<UserOrganization> qw1 = new QueryWrapper<UserOrganization>();
		qw1.eq("user_id", userId);
		qw1.eq("organization_id", orgId);
		qw1.eq("owner", true);
		if (userOrganizationMapper.selectOne(qw1) == null) {
			throw new RayinBusinessException("您无权操作该项目的成员设置，请联系该项目创建人！");
		}


		UpdateWrapper<UserOrganization> qw2 = new UpdateWrapper<UserOrganization>();
		qw2.eq("user_id", uo.getUserId()).eq("organization_id", uo.getOrganizationId());
		uo.setOwner(true);
		userOrganizationMapper.update(uo, qw2);

		UpdateWrapper<UserOrganization> qw3 = new UpdateWrapper<UserOrganization>();
		qw3.eq("user_id", userId).eq("organization_id", orgId);

		//TODO 通知移交用户
		Organization organization = organizationMapper.selectById(uo.getOrganizationId());
		Message message = new Message();
		message.setUserId(uo.getUserId());
		message.setInfo("您成为'" + organization.getOrganizationName() + "'项目所有者");
		message.setUrl("test");
		message.setChecked(false);
		return userOrganizationMapper.update(UserOrganization.builder().owner(false).build(), qw3);
	}

	/**
	 * 项目保存更新
	 * @param uo
	 * @return
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public int userOrganizationSave(UserOrganization uo, String userId, String organizationId) {
		Organization o = new Organization();
		o.setIcon(uo.getIcon());
		o.setIconColor(uo.getIconColor());
		o.setOrganizationName(uo.getOrganizationName());
		o.setThirdStorageAccessKey(uo.getThirdStorageAccessKey());
		o.setThirdStorageSecretKey(uo.getThirdStorageSecretKey());
		o.setThirdStorageUrl(uo.getThirdStorageUrl());
		o.setThirdStorageBucket(uo.getThirdStorageBucket());
		o.setThirdStorageResourceBucket(uo.getThirdStorageBucket());
		o.setOssType(uo.getOssType());
		o.setDeposit(uo.isDeposit());

		if (StringUtils.isBlank(uo.getOrganizationId())) {
			// 新增项目
			o.setCreateTime(new Date());
			o.setAccessKey(StringUtil.randomUUID());
			o.setSecretKey(StringUtil.randomUUID());
			organizationMapper.insert(o);
			uo.setOrganizationId(o.getOrganizationId());
			uo.setOwner(true);
			uo.setUserId(userId);
			//为项目创建存储桶
//			BucketDO bucketDO = new BucketDO();
//			bucketDO.setBucket(o.getOrganizationId());
//			bucketDO.setDescription(uo.getOrganizationName()+"文件桶");
//			bucketDO.setOwner("8fe35126fc6d442d9cf6acd4193553c9");
//			bucketDO.setAppSource(o.getOrganizationId());

//			try {
//				String result = cmsClient.createBucket(o.getOrganizationId(),o.getOrganizationName());
//			} catch (CmsException e) {
//				e.printStackTrace();
//				return 0;
//			}
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				throw e;
//			}
			//为桶创建索引
//			List<String> mapping = new ArrayList<>();
//			mapping.add("sys_id");
//			mapping.add("organization_id");
//			mapping.add("transaction_no");
//			mapping.add("blockChain_hash");
//			mapping.add("template_id");
//			mapping.add("template_version");
//			mapping.add("file_type");
//			mapping.add("quality");
//			mapping.add("quality_info");
//			mapping.add("receipt");
//			mapping.add("template_alias");

//			try {
//				String res = cmsClient.createMapping(o.getOrganizationId(),mapping);
//			} catch (CmsException e) {
//				throw e;
//			}

			//TODO 为项目创建资源根目录
//			File file = new File(folder);
//			InputStream inputStreamMd5;
//			String md5 = "";
//			try {
//				inputStreamMd5 = ResourceUtil.getResourceAsStream("folder.text");
//				md5 = DigestUtils.md5Hex(inputStreamMd5);
//			} catch (FileNotFoundException e) {
//				throw e;
//			} catch (IOException e) {
//				throw e;
//			}
//			InputStream inputStream = null;
//			try {
//				inputStream = ResourceUtil.getResourceAsStream("folder.text");
//			} catch (FileNotFoundException e) {
//				throw e;
//			} catch (IOException e) {
//				throw e;
//			}
//			Map<String, Object> map = new HashMap<>();
//			map.put("sys_id", "rayin");
//			map.put("organization_id", o.getOrganizationId());
//			map.put("folder_level", "0");
//			map.put("folder_id", "root");
//			map.put("folder", "/");
//			map.put("father_folder_id", "/");
//			map.put("data_type", "folder");
//			map.put("data_name", "/");
//			Map<String, Object> cm = null;
//			try {
//				cm = cmsClient.simpleUpload(Constants.CMS_RESOURCE_BUCKET,true,md5,inputStream,map);
//			} catch (CmsException e) {
//				if(e.getMessage().indexOf("code=722") > 0){
//					List<String> rsMapping = new ArrayList<>();
//					rsMapping.add("sys_id");
//					rsMapping.add("organization_id");
//					rsMapping.add("folder_level");
//					rsMapping.add("folder_id");
//					rsMapping.add("folder");
//					rsMapping.add("father_folder_id");
//					rsMapping.add("data_type");
//					rsMapping.add("data_name");
//					cmsClient.createMapping(Constants.CMS_RESOURCE_BUCKET,rsMapping);
//					inputStream = ResourceUtil.getResourceAsStream("folder.text");
//					cm = cmsClient.simpleUpload(Constants.CMS_RESOURCE_BUCKET,true,md5,inputStream,map);
//				}else{
//					throw e;
//				}
//			}
			return userOrganizationMapper.insert(uo);
		}

		if (!uo.isOwner()) {
			throw new RayinBusinessException("你不是该项目的拥有者，不能修改该项目！");
		}

		UpdateWrapper<Organization> uw1 = new UpdateWrapper<Organization>();
		uw1.eq("organization_id", uo.getOrganizationId());

		return organizationMapper.update(o, uw1);
	}

	/**
	 * 项目彻底删除
	 *
	 * @param uo
	 * @return
	 */
	@Override
	@Async
	@Transactional(rollbackFor = Exception.class)
	public void userOrganizationDel(UserOrganization uo, String userId) {
		if (!uo.isOwner()) {
			throw new RayinBusinessException("你不是该项目的拥有者，不能删除该项目！");
		}
		UpdateWrapper uw = new UpdateWrapper<>();
		uw.eq("organization_id", uo.getOrganizationId());
		log.info("项目：" + uo.getOrganizationId() + "被删除！操作用户为：" + userId);

		userElementMapper.delete(uw);
		userElementVersionHistoryMapper.delete(uw);
		userTemplateElementMapper.delete(uw);
		userTemplateMapper.delete(uw);
		userOrganizationMapper.delete(uw);
		organizationMapper.delete(uw);
	}

	/**
	 * 对象存储测试
	 * @param uo
	 * @param userId
	 * @param organizationId
	 */
//	@Override
//	public void  userOrganizationThirdStorageTest(UserOrganization uo, String userId, String organizationId) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//
//		File file = new File(ResourceUtil.getResourceAbsolutePathByClassPath("storage_test.pdf"));
//		switch (uo.getOssType()){
//			case "minio":
//				MinioClient minioClient = MinioClient.builder().endpoint(uo.getThirdStorageUrl())
//						.credentials(uo.getThirdStorageAccessKey(),uo.getThirdStorageSecretKey())
//						.build();
//				//File file = new File(ResourceUtil.getResourceAbsolutePathByClassPath("storage_test.pdf"));
//				InputStream in1 = new FileInputStream(file);
//
//				// 使用putObject上传一个文件到存储桶中。
//				ObjectWriteResponse objectWriteResponse = minioClient.putObject(
//						PutObjectArgs.builder().bucket(uo.getThirdStorageBucket()).object(file.getName()).stream(
//										in1, file.length(), -1)
//								//文件类型，可不传，传了之后，浏览器访问分享文件时候，response header会返回"Content-Type: image/png"，方便预览
//								.contentType("application/pdf")
//								.build());
//				objectWriteResponse = minioClient.putObject(
//						PutObjectArgs.builder().bucket(uo.getThirdStorageResourceBucket()).object(file.getName()).stream(
//										in1, file.length(), -1)
//								//文件类型，可不传，传了之后，浏览器访问分享文件时候，response header会返回"Content-Type: image/png"，方便预览
//								.contentType("application/pdf")
//								.build());
//
//				break;
//
//			case "aliyun-oss":
//				OSS client = new OSSClientBuilder().build(uo.getThirdStorageUrl(), uo.getThirdStorageAccessKey(), uo.getThirdStorageSecretKey());
//				InputStream in3 = ResourceUtil.getResourceAsStream("storage_test.pdf");
//				client.putObject(uo.getThirdStorageBucket(), uo.getOrganizationName() + "_push_test.pdf", in3);
//				InputStream in4 = ResourceUtil.getResourceAsStream("storage_test.pdf");
//				client.putObject(uo.getThirdStorageResourceBucket(), uo.getOrganizationName() + "_push_test.pdf", in4);
//				client.shutdown();
//				break;
//			case "tecent-cos":
//				COSCredentials cosCredentials = new BasicCOSCredentials(uo.getAccessKey(), uo.getSecretKey());
//				ClientConfig clientConfig = new ClientConfig();
//				COSClient cosClient = new COSClient(cosCredentials, clientConfig);
//				PutObjectRequest putObjectRequest = new PutObjectRequest(uo.getThirdStorageBucket(), uo.getAccessKey(), file);
//				putObjectRequest.setStorageClass(StorageClass.Standard);
//				PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
//				String etag = putObjectResult.getETag();
//				log.debug(etag);
//				putObjectRequest = new PutObjectRequest(uo.getThirdStorageResourceBucket(), uo.getAccessKey(), file);
//				putObjectRequest.setStorageClass(StorageClass.Standard);
//				putObjectResult = cosClient.putObject(putObjectRequest);
//				etag = putObjectResult.getETag();
//				log.debug(etag);
//				cosClient.shutdown();
//
//		}


//		File temp = File.createTempFile(uo.getOrganizationName() + "_push_test","txt");
//		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(temp));
//		bufferedWriter.write("项目：" + uo.getOrganizationName() + "，对象存储配置测试成功！");
//		bufferedWriter.close();
//		String filein = "项目：" + uo.getOrganizationName() + "，对象存储配置测试！";
//		RandomAccessFile mm = null;
//		mm = new RandomAccessFile(uo.getOrganizationName() + "_push_test.txt","rw");
//		mm.writeUTF(filein);
	//InputStream inputStream = new FileInputStream(temp);

//		client.putObject(uo.getThirdStorageResourceBucket(), uo.getOrganizationName() + "_push_test.txt", inputStream,
//				Long.valueOf(inputStream.available()), null, null,
//				"application/octet-stream");
	//client.putObject(uo.getThirdStorageBucket(), uo.getOrganizationName() + "_push_test.pdf", ResourceLoader.getResourceStream("storage_test.pdf"));
	//client.putObject(uo.getThirdStorageResourceBucket(), uo.getOrganizationName() + "_push_test.txt", inputStream);

	//temp.deleteOnExit();
	//}

	/**
	 * 对象存储测试
	 * @param uo
	 * @param userId
	 * @param organizationId
	 */
	@Override
	public void  userOrganizationThirdStorageTest(UserOrganization uo, String userId, String organizationId) throws IOException {
		File file = new File(ResourceUtil.getResourceAbsolutePathByClassPath("storage_test.pdf"));
		log.debug(JSON.toJSONString(ossBuilder.template().putFile(uo.getThirdStorageBucket(),file.getName(),new FileInputStream(file))));
	}
}
