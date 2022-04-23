
package ink.rayin.app.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ink.rayin.app.web.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * [构件管理服务接口].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-02-15 Wang Zhu create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
public interface IUserElementService {

	/**
	 * 构件列表查询分页
	 *
	 * @param page
	 * @param page
	 * @return
	 */
	IPage<UserElement> userElementQuery(String userId, IPage<UserElement> page, UserElement uem);

	/**
	 * 保存构件
	 *
	 * @param uem
	 * @return
	 */
	String userElementSave(UserElement uem) throws IOException;

	/**
	 * 收藏构件
	 *
	 * @param uem
	 * @return
	 */
	int userElementColle(UserElementFavorites uem, String orgId, String userId);

	/**
	 * 更新构件
	 *
	 * @param userId
	 * @param uem
	 * @return
	 */
	String userElementUpdate(String userId, UserElement uem) throws IOException, InstantiationException, IllegalAccessException;


	/**
	 * 新版本构件新增
	 * 先将原始版本移动至历史表，然后再删除原有表，重新插入新版本构件
	 * @param uem
	 * @return
	 */
	String userElementNewVersionSave(UserElement uem) throws IllegalAccessException, IOException, InstantiationException;

	/**
	 * 构件逻辑删除
	 * @param uem
	 * @return
	 */
    @Transactional(rollbackFor = Exception.class)
    int userElementLogicalDel(UserElement uem, String orgId, String userId);

	/**
	 * 构件彻底删除
	 * @param uem
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	int userElementDel(UserElement uem, String orgId, String userId);

	/**
	 * 构件恢复
	 * @param uem
	 * @return
	 */
	int userElementResume(UserElement uem);

	/**
	 * 构件版本恢复
	 * @param uem
	 * @return
	 */
	int userElementVersionResume(String userId, UserElementVersionHistory uem) throws IllegalAccessException, IOException, InstantiationException;

	IPage<UserElementVersionHistory>  userElementVersionList(Page page,
                                                             UserElementVersionHistory uem);


	IPage<UserTemplate> userElementTemplateRelationsQuery(Page page, UserTemplateElement userTemplateElement);

	IPage<UserElementFavorites> userElementFavoritesQuery(Page page, UserElementFavorites userElementFavorites);

	/**
	 * 收集构件删除
	 * @param uem
	 * @return
	 */
	int userElementFavoritesDel(UserElementFavorites uem);

	/**
	 * 将构件同步至模板
	 * @param ue
	 * @return
	 */
	int userElementSyncElTl(UserTemplateElement ue,String userId) throws IllegalAccessException, IOException, InstantiationException;


	/**
	 * 将构件同步至模板日志
	 * @param page
	 * @param orgId
	 * @return
	 */
	 IPage<UserElementSyncLog>  userElementSyncElTlLogQuery(Page page, String orgId,String elementId);


	/**
	 * 构件修改历史查询
	 * @param page
	 * @param orgId
	 * @param elementId
	 * @return
	 */
	 IPage<UserElementModifyHistory>  userElementModifyHistoryQuery(Page page, String orgId, String elementId);

	/**
	 * 构件导入
	 * @param overwriteFlag
	 * @param element
	 * @param userId
	 * @param orgId
	 * @return
	 */
	int userElementImport(boolean overwriteFlag, String element,String userId,String orgId) throws UnsupportedEncodingException;

	/**
	 * 构件分享
	 * @param elementId
	 * @param userId
	 * @return
	 */
	int share(ElementShare elementId,String userId);

	/**
	 * 取消构件分享
	 * @param elementId
	 * @param userId
	 * @return
	 */
	int cancelShare(ElementShare elementId,String userId);
}
