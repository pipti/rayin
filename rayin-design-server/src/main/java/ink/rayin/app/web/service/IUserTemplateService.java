
package ink.rayin.app.web.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import ink.rayin.app.web.model.UserTemplate;
import ink.rayin.app.web.model.UserTemplateApprove;

import java.io.IOException;

/**
 * [模板管理服务接口].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-03-02 Wang Zhu create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
public interface IUserTemplateService {

	/**
	 * 构件列表查询分页
	 *
	 * @param page
	 * @param page
	 * @return
	 */
	IPage<UserTemplate> userTemplateQuery(IPage<UserTemplate> page, UserTemplate uem);

	/**
	 * 保存构件
	 *
	 * @param uem
	 * @return
	 */
	int userTemplateSave(UserTemplate uem) throws IllegalAccessException, IOException, InstantiationException;

	/**
	 * 模板逻辑删除
	 * @param uem
	 * @return
	 */
	int userTemplateLogicalDel(UserTemplate uem);

	/**
	 * 模板物理删除
	 * @param ut
	 * @return
	 */

	int userTemplateDel(UserTemplate ut);
	/**
	 * 模板测试数据保存
	 * @param ut
	 * @return
	 */
	int userTemplateTestDataSave(UserTemplate ut);

	/**
	 * 模板测试预览
	 * @param ut
	 * @return
	 */
	JSONObject userTemplateView(UserTemplate ut) throws Exception;

	/**
	 * 模板测试存入存储
	 * @param ut
	 * @return
	 */
	JSONObject templateGenerate(UserTemplate ut) throws Exception;
	/**
	 * 模板恢复
	 * @param uem
	 * @return
	 */
	int userTemplateResume(UserTemplate uem);
	/**
	 * 线上模板导出
	 * @param ut
	 * @return
	 */
	UserTemplate userTemplateOnlineExport(UserTemplate ut);

	/**
	 * 线下模板导出
	 * @param ut
	 * @return
	 */
	String userTemplateOfflineExport(UserTemplate ut);
	/**
	 * 模板导入
	 * @param ut
	 * @return
	 */
	int userTemplateImport(String userId, String orgId,UserTemplate ut,boolean newTemplate);

	/**
	 * 模板同步申请
	 * @param userId
	 * @param orgId
	 * @param parameter
	 * @return
	 */
	int userTemplatePublishToOtherOrgPendingApproval(String userId,String orgId, UserTemplateApprove parameter)
			throws IllegalAccessException, IOException, InstantiationException ;

	/**
	 * 模板待审核查询
	 *
	 * @param page
	 * @param uem
	 * @return
	 */
	IPage<UserTemplateApprove> userTemplateApproveWaitQuery(IPage<UserTemplateApprove> page, UserTemplateApprove uem);

	/**
	 * 模板完成审核查询
	 *
	 * @param page
	 * @param uem
	 * @return
	 */
	IPage<UserTemplateApprove> userTemplateApproveFinishQuery(IPage<UserTemplateApprove> page, UserTemplateApprove uem);


	JSONObject userTemplateApprovePdfView(UserTemplateApprove utap) throws Exception;


	int userTemplateApproveCheck(String userId,String orgId,UserTemplateApprove parameter) throws IllegalAccessException, IOException, InstantiationException;
}
