package ink.rayin.app.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.rayin.app.web.model.UserOrganization;
import ink.rayin.app.web.model.Users;
import io.minio.errors.*;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * [用户项目管理服务接口].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-04-01 Wang Zhu create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
public interface IUserOrganizationService {



	/**
	 * 用户项目查询分页
	 *
	 * @param page uo
	 * @param o
	 * @return
	 */
	IPage<UserOrganization> userOrganizationQuery(IPage<UserOrganization> page, UserOrganization o);

	/**
	 * 用户项目查询
	 *
	 * @param uo
	 * @return
	 */
	UserOrganization userOrganizationQueryOne(UserOrganization uo);

	/**
	 * 项目成员查询分页
	 *
	 * @param page uo
	 * @param uo
	 * @return
	 */
	IPage<UserOrganization> userMemberQuery(IPage<UserOrganization> page, UserOrganization uo);

	/**
	 * 项目成员添加
	 *
	 * @param uem
	 * @return
	 */
	int userOrganizationMemberAdd(UserOrganization uem);

	/**
	 * 项目添加成员查询
	 *
	 * @param page,uo
	 * @return
	 */
	IPage<Users> userAddMemberQuery(IPage<Users> page, Users uo);

	/**
	 * 项目成员删除
	 *
	 * @param uo
	 * @param userId
	 * @param orgId
	 * @return
	 */
	int userOrganizationMemberDel(UserOrganization uo,String userId,String orgId);

	/**
	 * 项目权利移交
	 *
	 * @param uo
	 * @param userId
	 * @param orgId
	 * @return
	 */
	int userOrganizationRightTrans(UserOrganization uo,String userId,String orgId);

	/**
	 * 项目更新保存
	 *
	 * @param uo
	 * @param userId
	 * @param organizationId
	 * @return
	 */
	int userOrganizationSave(UserOrganization uo,String userId,String organizationId) throws Exception;

	/**
	 * 项目彻底删除
	 * 异步删除，无返回值
	 * @param uo
	 * @param userId
	 * @return
	 */
	void userOrganizationDel(UserOrganization uo,String userId);

	void userOrganizationThirdStorageTest(UserOrganization uo,String userId,String organizationId) throws InvalidPortException,
			InvalidEndpointException, IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException,
			InvalidResponseException, InternalException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException,
			XmlParserException;
}
