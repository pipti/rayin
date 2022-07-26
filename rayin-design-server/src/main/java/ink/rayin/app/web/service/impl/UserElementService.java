
package ink.rayin.app.web.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import gui.ava.html.Html2Image;
import ink.rayin.app.web.cache.KeyUtil;
import ink.rayin.app.web.cache.lock.support.SynchronizedRedisLockSupport;
import ink.rayin.app.web.exception.BusinessCodeMessage;
import ink.rayin.app.web.exception.RayinBusinessException;
import ink.rayin.htmladapter.base.model.tplconfig.Element;
import ink.rayin.htmladapter.base.model.tplconfig.TemplateConfig;
import ink.rayin.app.web.model.*;
import ink.rayin.app.web.dao.*;
import ink.rayin.app.web.service.IUserElementService;
import ink.rayin.tools.utils.Base64Util;
import ink.rayin.tools.utils.BeanConvert;
import ink.rayin.tools.utils.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.List;


/**
 * [构件管理服务类].
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
public class UserElementService implements IUserElementService{
	private static Logger logger = LoggerFactory.getLogger(UserElementService.class);
	@Resource
	private UserElementMapper userElementMapper;
	@Resource
	private UserElementVersionHistoryMapper userElementVersionHistoryMapper;
	@Resource
	private UserElementModifyHistoryMapper userElementModifyHistoryMapper;
	@Resource
	private UserTemplateElementMapper userTemplateElementMapper;
	@Resource
	private UserElementFavoritesMapper userElementFavoritesMapper;
	@Resource
	private UsersMapper usersMapper;
	@Resource
	private UserInfoMapper userInfoMapper;
	@Resource
	private UserTemplateMapper userTemplateMapper;
	@Resource
	private UserElementSyncLogMapper userElementSyncLogMapper;

	@Resource
	private ElementShareMapper elementShareMapper;

	@Resource
	private ElementLikesMapper elementLikesMapper;

	@Resource
	@Qualifier("synchronizedRedisLockSupport")
	private SynchronizedRedisLockSupport synchronizedRedisLockSupport;
	/**
	 * 组件列表查询自定义分页
	 *
	 * @param page
	 * @param page
	 * @return
	 */
	@Override
	public IPage<UserElement> userElementQuery(String userId, IPage<UserElement> page, UserElement uem){
		QueryWrapper<UserElement> qw = new QueryWrapper<UserElement>();
		//IPage<UserElement> userElementList;
		if(StringUtils.isBlank(uem.getName())){
			qw.eq("organization_id",uem.getOrganizationId()).eq("del_flag",uem.getDelFlag())
					.orderByDesc("create_time").orderByDesc("update_time");
		}else{
			qw.eq("organization_id",uem.getOrganizationId()).eq("del_flag",uem.getDelFlag())
					.and(i->i.like("name", uem.getName()).or().like("memo",uem.getName())
							.or().like("user_name",uem.getName())
							.or().like("element_id",uem.getName()))
			.orderByDesc("create_time").orderByDesc("update_time");
		}

		IPage<UserElement> userElementList = userElementMapper.selectPage(page,qw);
		List<UserElement> userElements = userElementList.getRecords();
		userElements.forEach(k->{
			if(k.getUserId().equals(userId)){
				k.setOwner(true);
			}else{
				k.setOwner(false);
			}
			int collectCount = userElementFavoritesMapper.selectCount(new QueryWrapper<UserElementFavorites>().lambda().
                    eq(UserElementFavorites::getElementId,k.getElementId()).
                    eq(UserElementFavorites::getOrganizationId,k.getOrganizationId()).
                    eq(UserElementFavorites::getUserId,userId));
            k.setColled(collectCount > 0?true:false);
		});
		return userElementList;
	}

	/**
	 * 新增构件
	 * @param uem
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String userElementSave(UserElement uem) throws IOException {
		Users users = usersMapper.selectById(uem.getUserId());
		uem.setUserName(users.getUsername());


		Html2Image html2Image = Html2Image.fromHtml(uem.getContent());
		final Base64.Encoder encoder = Base64.getEncoder();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		html2Image.getImageRenderer().setAutoHeight(true);

		ImageIO.write(html2Image.getImageRenderer().getBufferedImage(), "png", baos);

		byte[] bytes = baos.toByteArray();
		uem.setElementThum("data:image/png;base64," + encoder.encodeToString(bytes));
		logger.debug("构件缩略图大小"+ (bytes.length/8/1024));
		userElementMapper.insert(uem);
		return uem.getElementId();
	}

	/**
	 * 收藏构件
	 * @param uef
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userElementColle(UserElementFavorites uef,String orgId,String userId){
		QueryWrapper<UserElementFavorites> qw = new QueryWrapper<UserElementFavorites>();
		qw.eq("user_id",userId);
		qw.eq("organization_id",orgId);
		qw.eq("element_id",uef.getElementId());
		int i = 0;
		if(userElementFavoritesMapper.selectCount(qw) > 0){
			i = userElementFavoritesMapper.delete(qw);
		}else{
			uef.setCreateTime(new Date());
			uef.setUpdateTime(null);
			uef.setUserId(userId);
			i = userElementFavoritesMapper.insert(uef);
		}
		return i;
	}

	/**
	 * 更新构件
	 * @param uem
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String userElementUpdate(String userId, UserElement uem) throws IOException, InstantiationException, IllegalAccessException {
		//TODO 锁定状态不允许其他人更新
		// uw.eq("user_id",uem.getUserId());
		Users users = usersMapper.selectById(userId);
		uem.setUpdateUserName(users.getUsername());
		uem.setUpdateUserId(userId);
		// 插入修改历史表
		QueryWrapper<UserElement> qw = new QueryWrapper<UserElement>();
		//qw.eq("user_id",userElement.getUserId())
		qw.eq("element_id",uem.getElementId())
				.eq("element_version",uem.getElementVersion())
				.eq("organization_id",uem.getOrganizationId());
		UserElement ueo = userElementMapper.selectOne(qw);
		UserElementModifyHistory uemh;

		if(ueo != null){
			uemh = BeanConvert.convert(ueo,UserElementModifyHistory.class);
			userElementModifyHistoryMapper.insert(uemh);
		}

		// 更新原有记录
		UpdateWrapper uw = new UpdateWrapper();
		uw.eq("element_id",uem.getElementId());
		uw.eq("element_version",uem.getElementVersion());
		uw.eq("organization_id",uem.getOrganizationId());

		Html2Image html2Image = Html2Image.fromHtml(uem.getContent());
		final Base64.Encoder encoder = Base64.getEncoder();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		html2Image.getImageRenderer().setAutoHeight(true);
		ImageIO.write(html2Image.getImageRenderer().getBufferedImage(), "png", baos);

		byte[] bytes = baos.toByteArray();
		uem.setElementThum("data:image/jpeg;base64," + encoder.encodeToString(bytes));


		userElementMapper.update(uem,uw);
		return uem.getElementId();
	}


	/**
	 * 新版本构件新增
	 * 先将原始版本移动至历史表，然后再删除原有表，重新插入新版本构件
	 * @param userElement
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String userElementNewVersionSave(UserElement userElement) throws IllegalAccessException, IOException, InstantiationException {
		// 插入version历史表
		QueryWrapper<UserElement> qw = new QueryWrapper<UserElement>();
		//qw.eq("user_id",userElement.getUserId())
		qw.eq("element_id",userElement.getElementId())
				.eq("element_version",userElement.getElementVersion())
				.eq("organization_id",userElement.getOrganizationId());
		UserElement ueo = userElementMapper.selectOne(qw);
		UserElementVersionHistory uevh;
		UserElementModifyHistory uemh;
		if(ueo != null){
			uevh = BeanConvert.convert(ueo,UserElementVersionHistory.class);
			userElementVersionHistoryMapper.insert(uevh);

			uemh = BeanConvert.convert(ueo,UserElementModifyHistory.class);
			userElementModifyHistoryMapper.insert(uemh);

			// 删除原有版本
			userElementMapper.delete(qw);
			// 插入新版本
			userElement.setCreateTime(new Date());
			userElement.setElementVersion((Double.parseDouble(userElement.getElementVersion()) + 0.01) + "");
			try {
				Html2Image html2Image = Html2Image.fromHtml(userElement.getContent());
				final Base64.Encoder encoder = Base64.getEncoder();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				html2Image.getImageRenderer().setAutoHeight(true);
				ImageIO.write(html2Image.getImageRenderer().getBufferedImage(), "png", baos);

				byte[] bytes = baos.toByteArray();
				userElement.setElementThum("data:image/jpeg;base64," + encoder.encodeToString(bytes));
				logger.debug("构件缩略图大小" + (bytes.length / 8 / 1024));
			}catch(Exception e){
				logger.error(e.getMessage(),e);
			}
			userElementMapper.insert(userElement);
			return userElement.getElementId();
		}else{
			return "";
		}
	}


	/**
	 * 构件逻辑删除
	 * @param uem
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userElementLogicalDel(UserElement uem, String orgId, String userId){
		return userElementMapper.update(null,Wrappers.<UserElement>update().lambda().set(UserElement::getDelFlag,"1")
				//TODO 锁定状态不允许其他人删除
//				.eq(UserElement::getUserId, uem.getUserId())
				.eq(UserElement::getElementVersion,uem.getElementVersion())
				.eq(UserElement::getElementId,uem.getElementId())
				.eq(UserElement::getOrganizationId, orgId)
				);

	}

	/**
	 * 构件彻底删除
	 * @param uem
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userElementDel(UserElement uem, String orgId, String userId){
//		UpdateWrapper<UserElement> qw = new UpdateWrapper<UserElement>();
//		qw.eq("user_id",uem.getUserId()).eq("element_id",uem.getElementId());

//		UpdateWrapper<UserElementVersionHistory> uevhWrapper = new UpdateWrapper<UserElementVersionHistory>();
//		uevhWrapper.eq("user_id",uem.getUserId()).eq("element_id",uem.getElementId())
//				.eq("element_version",uem.getElementVersion());

		userElementVersionHistoryMapper.delete(Wrappers.<UserElementVersionHistory>update().lambda()
				.eq(UserElementVersionHistory::getUserId,uem.getUserId())
				.eq(UserElementVersionHistory::getElementId,uem.getElementId())
				.eq(UserElementVersionHistory::getElementVersion,uem.getElementVersion())
				.eq(UserElementVersionHistory::getOrganizationId,orgId));

		return userElementMapper.delete(Wrappers.<UserElement>update().lambda()
				.eq(UserElement::getUserId,uem.getUserId())
				.eq(UserElement::getElementId,uem.getElementId())
				.eq(UserElement::getOrganizationId,orgId));
	}

	/**
	 * 构件恢复
	 * @param uem
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userElementResume(UserElement uem){
		return userElementMapper.update(null,Wrappers.<UserElement>update().lambda().set(UserElement::getDelFlag,"0")
				.eq(UserElement::getUserId, uem.getUserId())
				.eq(UserElement::getElementVersion,uem.getElementVersion())
				.eq(UserElement::getElementId,uem.getElementId()));
	}
	/**
	 * 构件恢复
	 * @param uevh
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userElementVersionResume(String userId,UserElementVersionHistory uevh) throws IllegalAccessException, IOException, InstantiationException {
		// 插入构件modify日志
		QueryWrapper<UserElement> qw = new QueryWrapper<UserElement>();
		qw.eq("user_id",uevh.getUserId())
				.eq("element_id",uevh.getElementId())
				.eq("organization_id",uevh.getOrganizationId());
		UserElement beResumedEl = userElementMapper.selectOne(qw);
		String currentVersion = beResumedEl.getElementVersion();
		UserElementModifyHistory uemh = BeanConvert.convert(beResumedEl,UserElementModifyHistory.class);
		uemh.setOperationType("vresume");
		userElementModifyHistoryMapper.insert(uemh);

		// 插入现有版本记录至版本历史表
		UserElementVersionHistory uemvh = BeanConvert.convert(beResumedEl,UserElementVersionHistory.class);
		userElementVersionHistoryMapper.insert(uemvh);


		// 删除原有构件
		UpdateWrapper<UserElement> uqw = new UpdateWrapper<UserElement>();
		uqw.eq("user_id",uemh.getUserId())
				.eq("element_id",uemh.getElementId())
				.eq("organization_id",uemh.getOrganizationId());
		userElementMapper.delete(qw);

		// 插入恢复版本构件
		QueryWrapper<UserElementVersionHistory> uevhqw = new QueryWrapper<UserElementVersionHistory>();
		uevhqw.eq("user_id",uevh.getUserId())
				.eq("element_id",uevh.getElementId())
				.eq("organization_id",uevh.getOrganizationId())
				.eq("element_version",uevh.getElementVersion());
		UserElementVersionHistory uevhq = userElementVersionHistoryMapper.selectOne(uevhqw);
		UserElement willResumeEl =  BeanConvert.convert(uevhq,UserElement.class);
		willResumeEl.setUpdateUserId(userId);
		willResumeEl.setUpdateTime(new Date());
		Users users = usersMapper.selectById(userId);
		willResumeEl.setUpdateUserName(users.getUsername());
		willResumeEl.setElementVersion((Float.parseFloat(currentVersion) + 0.01) + "");

		return userElementMapper.insert(willResumeEl);
	}



	/**
	 * 构件版本历史查询
	 * @param uem
	 * @return
	 */
	@Override
	public IPage<UserElementVersionHistory>  userElementVersionList(Page page,
																	UserElementVersionHistory uem){
		return  userElementVersionHistoryMapper.selectPage(page,
				Wrappers.<UserElementVersionHistory>lambdaQuery().eq(UserElementVersionHistory::getUserId,uem.getUserId())
				.eq(UserElementVersionHistory::getElementId,uem.getElementId()));
	}

	/**
	 * 构件模板关系查询
	 * @param page
	 * @param userTemplateElement
	 * @return
	 */
	@Override
	public IPage<UserTemplate> userElementTemplateRelationsQuery(Page page,
																 UserTemplateElement userTemplateElement){
		return userTemplateElementMapper.userElementTemplateRelationsQuery(page,userTemplateElement);
	}


	/**
	 * 构件收藏查询
	 * @param page
	 * @param uef
	 * @return
	 */
	@Override
	public IPage<UserElementFavorites> userElementFavoritesQuery(Page page,
																 UserElementFavorites uef){
		QueryWrapper<UserElementFavorites> qw = new QueryWrapper<UserElementFavorites>();
		if(StringUtils.isBlank(uef.getName())){
			qw.eq("user_id",uef.getUserId()).eq("del_flag","0")
					.orderByDesc("create_time").orderByDesc("update_time");
		}else{
			qw.eq("user_id",uef.getUserId()).eq("del_flag","0")
					.and(i->i.like("name", uef.getName()).or().like("memo",uef.getName()))
					.orderByDesc("create_time").orderByDesc("update_time");
		}
		return userElementFavoritesMapper.selectPage(page,qw);
	}

	/**
	 * 构件逻辑删除
	 * @param uef
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userElementFavoritesDel(UserElementFavorites uef){
		UpdateWrapper<UserElementFavorites> qw = new UpdateWrapper<UserElementFavorites>();
		qw.eq("user_id",uef.getUserId()).eq("element_id",uef.getElementId());
		return userElementFavoritesMapper.delete(qw);
	}

	/**
	 * 构件同步对应模板
	 * @param uef
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userElementSyncElTl(UserTemplateElement uef,String userId) throws IllegalAccessException, IOException, InstantiationException {
		UserTemplate ut = new UserTemplate();
//		ut.setUserId(uef.getUserId());
		ut.setTemplateId(uef.getTemplateId());
		ut.setTemplateVersion(uef.getTemplateVersion());
		// 获取构件信息
		QueryWrapper<UserElement> ueqw = new QueryWrapper<UserElement>();
		ueqw.eq("element_id",uef.getElementId())
//				.eq("element_version",uef.getElementVersion())
				.eq("organization_id",uef.getOrganizationId());
		UserElement userElement = userElementMapper.selectOne(ueqw);

		if(userElement == null){
			throw new RayinBusinessException("同步的构件不存在或已升级版本");
		}
		// 获取模板
		QueryWrapper<UserTemplate> utqw = new QueryWrapper<UserTemplate>();
		utqw.eq("template_id",uef.getTemplateId())
				.eq("template_version",uef.getTemplateVersion())
				.eq("organization_id",uef.getOrganizationId());
		UserTemplate userTemplate = userTemplateMapper.selectOne(utqw);

		// 重新组装elconfig
		List<UserTemplateElement> ule = JSONObject.parseObject(userTemplate.getElConfig(), List.class);
		for(int i = 0;i < ule.size(); i++){
			UserTemplateElement utlei = ule.get(i);
			if(utlei.getElementId().equals(userElement.getElementId())
					&&
					ule.get(i).getOrganizationId().equals(userElement.getOrganizationId())
//					&& ule.get(i).getElementVersion().equals(userElement.getElementVersion())
			){
				utlei.setName(userElement.getName());
				utlei.setElementVersion(userElement.getElementVersion());
				utlei.setContent(userElement.getContent());
				utlei.setMemo(userElement.getMemo());
//				UserTemplateElement ute = BeanConvert.convert(userElement,UserTemplateElement.class);
//				ute.setSeq(utlei.getSeq());
//				ute.setElementAvaliableDataPath(utlei.getElementAvaliableDataPath());
//				ute.setPageNumIsDisplay(utlei.isPageNumIsDisplay());
//				ute.setPageNumFirstPage(utlei.isPageNumFirstPage());
//				ute.setPageNumIsCalculate(utlei.isPageNumIsCalculate());
//
//				ute.setAddBlankPage(utlei.isAddBlankPage());
//				ute.setUncommonCharsAnalysis(utlei.isUncommonCharsAnalysis());
//				ute.setPageNumDisplayPoss(utlei.getPageNumDisplayPoss());
				ule.set(i,utlei);
			}
		}
		// 重新组装tplconfig
		TemplateConfig templateConfig = JSONObject.parseObject(userTemplate.getTplConfig(),TemplateConfig.class);
		List<Element> le = templateConfig.getElements();
		for(int i = 0;i < ule.size(); i++){
			if(le.get(i).getElementId().equals(userElement.getElementId())
//					&&
//					le.get(i).getElementVersion().equals(userElement.getElementVersion())
			){
				Element eo = le.get(i);
				eo.setContent(userElement.getContent());
				eo.setName(userElement.getName());
				eo.setElementVersion(userElement.getElementVersion());
				le.set(i,eo);
			}
		}
		userTemplate.setElConfig(JSONObject.toJSONString(ule));
		userTemplate.setTplConfig(JSONObject.toJSONString(templateConfig));
		userTemplateMapper.update(userTemplate,utqw);

		// 更新user_template_element 信息
		UpdateWrapper uw = new UpdateWrapper();
		uw.eq("template_id",uef.getTemplateId());
		uw.eq("template_version",uef.getTemplateVersion());
		uw.eq("organization_id",uef.getOrganizationId());
		uw.eq("element_id",uef.getElementId());
		UserTemplateElement userTemplateElement = new UserTemplateElement();
		userTemplateElement.setElementVersion(userElement.getElementVersion());
		userTemplateElementMapper.update(userTemplateElement,uw);




		UserElementSyncLog uesl = new UserElementSyncLog();
		uesl.setElementId(uef.getElementId());
		uesl.setElementVersion(uef.getElementVersion());
		uesl.setTemplateId(uef.getTemplateId());
		uesl.setTemplateVersion(uef.getTemplateVersion());
//		uesl.setUserId(uef.getUserId());
		uesl.setSyncTime(new Date());
		uesl.setOrganizationId(uef.getOrganizationId());
		uesl.setOperUserId(userId);
		uesl.setTemplateName(uef.getName());
		userElementSyncLogMapper.insert(uesl);
		// 更新模板
		return userTemplateMapper.update(userTemplate,utqw);
	}


	/**
	 * 构件同步模板日志查询
	 * @param page
	 * @param orgId
	 * @param elementId
	 * @return
	 */
	@Override
	public IPage<UserElementSyncLog>  userElementSyncElTlLogQuery(Page page, String orgId, String elementId){

		return  userElementSyncLogMapper.selectPage(page,
				Wrappers.<UserElementSyncLog>lambdaQuery().eq(UserElementSyncLog::getElementId,elementId)
						.eq(UserElementSyncLog::getOrganizationId,orgId));
	}


	/**
	 * 构件修改历史查询
	 * @param page
	 * @param orgId
	 * @param elementId
	 * @return
	 */
	@Override
	public IPage<UserElementModifyHistory>  userElementModifyHistoryQuery(Page page, String orgId, String elementId){
		return  userElementModifyHistoryMapper.selectPage(page,
				Wrappers.<UserElementModifyHistory>lambdaQuery().eq(UserElementModifyHistory::getElementId,elementId)
						.eq(UserElementModifyHistory::getOrganizationId,orgId).orderByDesc(UserElementModifyHistory::getUpdateTime));
	}

	/**
	 * 构件导入
	 * @param element
	 * @param userId
	 * @param orgId
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userElementImport(boolean overwriteFlag, String element,String userId,String orgId) throws UnsupportedEncodingException {
		String elDataMd5 = element.substring(0,32);
		String elDataBase64 = element.substring(33);
		if(!elDataMd5.equals(DigestUtil.md5Hex(elDataBase64))){
			throw new RayinBusinessException("导入的构件非法，请确认是否为合法的构件导出文件！");
		}
		String elData =  Base64Util.decode(elDataBase64);
		UserElement ele = JSONObject.parseObject(elData,UserElement.class);

		int count;
		ele.setUserId(userId);
		ele.setOrganizationId(orgId);
		if(overwriteFlag){
			// 更新原有记录
			UpdateWrapper uw = new UpdateWrapper();
			uw.eq("element_id",ele.getElementId());
			uw.eq("element_version",ele.getElementVersion());
			uw.eq("organization_id",orgId);

			count = userElementMapper.update(ele,uw);
			ele.setUpdateTime(new Date());
			if(count == 0){
				ele.setUpdateTime(null);
				ele.setCreateTime(new Date());
				ele.setUserName(usersMapper.selectById(userId).getUsername());
				count = userElementMapper.insert(ele);
			}
		}else{
			//置空，系统重新生成ID
			ele.setElementId("");
			ele.setUpdateTime(null);
			ele.setCreateTime(new Date());
			ele.setUserName(usersMapper.selectById(userId).getUsername());
			count = userElementMapper.insert(ele);
		}
		return count;
	}

	/**
	 * 共享构件
	 * @param elementShare
	 * @param userId
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int share(ElementShare elementShare, String userId) {
		UserElement userElement = userElementMapper.selectOne(new QueryWrapper<UserElement>().lambda().eq(UserElement::getOrganizationId,elementShare.getOrganizationId())
		.eq(UserElement::getElementId,elementShare.getElementId()));
		if (!userElement.getUserId().equals(userId)) {
			throw new RayinBusinessException("只能分享自己的创建的构件");
		}

		elementShare.setElementId(userElement.getElementId());
		elementShare.setOrganizationId(userElement.getOrganizationId());
		elementShare.setUserId(userElement.getUserId());
		elementShare.setTitle(userElement.getName());
		UserInfo userInfo = userInfoMapper.selectById(userElement.getUserId());
		if(userInfo != null && StringUtils.isNotBlank(userInfo.getHeadImg())){
			elementShare.setHeadImg(userInfo.getHeadImg());
		}

		if(elementShareMapper.selectCount(Wrappers.<ElementShare>query().lambda().eq(ElementShare::getElementId,userElement.getElementId())
				.eq(ElementShare::getOrganizationId,userElement.getOrganizationId())
				.eq(ElementShare::getUserId,userElement.getUserId())) > 0){
			//更新
			 elementShareMapper.update(elementShare,Wrappers.<ElementShare>query().lambda().eq(ElementShare::getElementId,userElement.getElementId())
					.eq(ElementShare::getOrganizationId,userElement.getOrganizationId())
					.eq(ElementShare::getUserId,userElement.getUserId()));
		}else{
			//插入
			elementShare.setLikes(0);
			elementShare.setStores(0);
			elementShare.setViews(0);
			 elementShareMapper.insert(elementShare);
		}
		return userElementMapper.update(null, Wrappers.<UserElement>update().lambda().set(UserElement::isShareFlag,true)
				.eq(UserElement::getOrganizationId, userElement.getOrganizationId())
				.eq(UserElement::getUserId,userElement.getUserId())
				.eq(UserElement::getElementId,userElement.getElementId()));
	}

	/**
	 * 取消构件分享
	 * @param elementShare
	 * @param userId
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int cancelShare(ElementShare elementShare, String userId) {
		UserElement userElement = userElementMapper.selectOne(new QueryWrapper<UserElement>().lambda().eq(UserElement::getOrganizationId,elementShare.getOrganizationId())
				.eq(UserElement::getElementId,elementShare.getElementId()));
		if (!userElement.getUserId().equals(userId)) {
			throw new RayinBusinessException("构件所有者才能取消构件分享");
		}
		Wrappers.<ElementShare>query().lambda().eq(ElementShare::getElementId,userElement.getElementId())
				.eq(ElementShare::getOrganizationId,userElement.getOrganizationId())
				.eq(ElementShare::getUserId,userElement.getUserId());

		 elementShareMapper.delete(Wrappers.<ElementShare>query().lambda().eq(ElementShare::getElementId,userElement.getElementId())
				.eq(ElementShare::getOrganizationId,userElement.getOrganizationId())
				.eq(ElementShare::getUserId,userElement.getUserId()));

		return userElementMapper.update(null, Wrappers.<UserElement>update().lambda().set(UserElement::isShareFlag,false)
				.eq(UserElement::getOrganizationId, userElement.getOrganizationId())
				.eq(UserElement::getUserId,userElement.getUserId())
				.eq(UserElement::getElementId,userElement.getElementId()));
	}

	/**
	 * 构件预览
	 * @param elementShare
	 * @return
	 */
	public UserElement sharedView(ElementShare elementShare) {
		//check
		shareStatus(elementShare);
		QueryWrapper<ElementShare> elementShareQueryWrapper = new QueryWrapper<>();
		elementShareQueryWrapper.lambda()
                .eq(ElementShare::getOrganizationId,elementShare.getOrganizationId())
                .eq(ElementShare::getElementId,elementShare.getElementId());


		String key = KeyUtil.makeKey("Rayin","-","Like","Lock");
		//views
		//TODO if lock
		synchronizedRedisLockSupport.tryLock(key);
		try {
			ElementShare currentE2 = elementShareMapper.selectOne(elementShareQueryWrapper);
			currentE2.setViews(currentE2.getViews() + 1);
			elementShareMapper.update(currentE2,elementShareQueryWrapper);
		} catch (Exception e) {
		    e.printStackTrace();
			throw RayinBusinessException.buildBizException(BusinessCodeMessage.OTHERS);
		} finally {
			synchronizedRedisLockSupport.unlock(key);
		}
		return userElementMapper.selectOne(new QueryWrapper<UserElement>().lambda()
				.eq(UserElement::getOrganizationId,elementShare.getOrganizationId())
				.eq(UserElement::getElementId,elementShare.getElementId()));
	}

	/**
	 * 共享构件搜索
	 * @param elementShare
	 * @return
	 */
	public IPage<ElementShare> shareQuery(ElementShare elementShare,int pageCurrent, int pageSize) {
		QueryWrapper<ElementShare> queryWrapper = new QueryWrapper<>();
		LambdaQueryWrapper<ElementShare>  lambdaQueryWrapper = queryWrapper.lambda();

		if (StringUtils.isNotBlank(elementShare.getNote())) {
			lambdaQueryWrapper.like(ElementShare::getNote,elementShare.getNote());
		}
		if (elementShare.getStores() == -1) {
			lambdaQueryWrapper.orderByDesc(ElementShare::getStores);
		} else if (elementShare.getStores() == 1) {
			lambdaQueryWrapper.orderByAsc(ElementShare::getStores);
		}
		if (elementShare.getViews() == -1) {
			lambdaQueryWrapper.orderByDesc(ElementShare::getViews);
		} else if (elementShare.getViews() == 1) {
			lambdaQueryWrapper.orderByAsc(ElementShare::getViews);
		}
		if (elementShare.getLikes() == -1) {
			lambdaQueryWrapper.orderByDesc(ElementShare::getLikes);
		} else if (elementShare.getLikes() == 1) {
			lambdaQueryWrapper.orderByAsc(ElementShare::getLikes);
		}
		Page page = new Page(pageCurrent, pageSize);
		UserInfo userInfo;
		IPage<ElementShare> ret = elementShareMapper.selectPage(page,queryWrapper);
		if (ret.getRecords().size()>0) {
			ret.getRecords().stream().forEach(elementShare1 -> {
				UserElement userElement = userElementMapper.selectOne(new QueryWrapper<UserElement>().lambda()
				.eq(UserElement::getElementId,elementShare1.getElementId())
				.eq(UserElement::getOrganizationId,elementShare1.getOrganizationId()));
				if(userElement != null) {
					elementShare1.setUserName(userElement.getUserName());
					UserInfo userInfoEl = userInfoMapper.selectById(userElement.getUserId());
					elementShare1.setNickName(userInfoEl.getNickName());
					elementShare1.setMoblie(userInfoEl.getPhone());
					elementShare1.setElementThum(userElement.getElementThum());
				}
			});
		};
		return ret;
	}


	/**
	 * 共享构件收藏
	 * @param elementShare
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean copyElement(ElementShare elementShare,String userId) {
		//check
		shareStatus(elementShare);
		if (userElementFavoritesMapper.selectOne(
				new QueryWrapper<UserElementFavorites>().lambda()
						.eq(UserElementFavorites::getElementId,elementShare.getElementId())
						.eq(UserElementFavorites::getOrganizationId,elementShare.getOrganizationId())
		)!= null) {
			return true;
		}
		QueryWrapper<ElementShare> elementShareQueryWrapper = new QueryWrapper<>();
		elementShareQueryWrapper.lambda()
                .eq(ElementShare::getOrganizationId,elementShare.getOrganizationId())
                .eq(ElementShare::getElementId,elementShare.getElementId());

		String key = KeyUtil.makeKey("Rayin","-","Memo","Lock");
		//momeTime
		synchronizedRedisLockSupport.tryLock(key);
		try {
			ElementShare currentE2 = elementShareMapper.selectOne(elementShareQueryWrapper);
			currentE2.setStores(currentE2.getStores() + 1);
			elementShareMapper.update(currentE2,elementShareQueryWrapper);
		} catch (Exception e) {
			throw new RayinBusinessException("收藏失败");
		} finally {
			synchronizedRedisLockSupport.unlock(key);
		}
		UserElement userElement = userElementMapper.selectOne(new QueryWrapper<UserElement>().lambda().eq(UserElement::getOrganizationId,elementShare.getOrganizationId())
				.eq(UserElement::getElementId,elementShare.getElementId()));
		UserElementFavorites userElementFavorites = new UserElementFavorites();
		userElementFavorites.setUserId(userId);
		userElementFavorites.setElementId(elementShare.getElementId());
		userElementFavorites.setOrganizationId(elementShare.getOrganizationId());
		userElementFavorites.setName(userElement.getName());
		userElementFavorites.setMemo(userElement.getMemo());
		userElementFavorites.setContent(userElement.getContent());
		userElementFavorites.setTestData(userElement.getTestData());
		userElementFavorites.setCreateTime(new Date());
		userElementFavoritesMapper.insert(userElementFavorites);
		return true;
	}

	/**
	 * 共享构件点赞
	 * @param elementShare
	 * @param userId
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean like(ElementShare elementShare, String userId) {
		//check
		shareStatus(elementShare);
		QueryWrapper<ElementShare> elementShareQueryWrapper = new QueryWrapper<>();
		elementShareQueryWrapper.lambda()
                .eq(ElementShare::getOrganizationId,elementShare.getOrganizationId())
                .eq(ElementShare::getElementId,elementShare.getElementId());
		if (elementLikesMapper.selectOne(
				new QueryWrapper<ElementLikes>().lambda().
						eq(ElementLikes::getElementId,elementShare.getElementId()).
						eq(ElementLikes::getOrganizationId,elementShare.getOrganizationId()).
						eq(ElementLikes::getUserId,userId)
		)!= null) {
			return true;
		}
		String key = KeyUtil.makeKey("Rayin","-","Like","Lock");
		//likes
		synchronizedRedisLockSupport.tryLock(key);
		try {
			ElementShare currentE2 = elementShareMapper.selectOne(elementShareQueryWrapper);
			currentE2.setLikes(currentE2.getLikes() + 1);
			elementShareMapper.update(currentE2,elementShareQueryWrapper);
		} catch (Exception e) {
			throw RayinBusinessException.buildBizException(BusinessCodeMessage.OTHERS);
		} finally {
			synchronizedRedisLockSupport.unlock(key);
		}

		ElementLikes elementLikes = new ElementLikes();
		elementLikes.setElementId(elementShare.getElementId());
		elementLikes.setOrganizationId(elementShare.getOrganizationId());
		elementLikes.setUserId(userId);
		elementLikesMapper.insert(elementLikes);
		return true;
	}
	private void shareStatus(ElementShare elementShare) {
		QueryWrapper<ElementShare> elementShareQueryWrapper = new QueryWrapper<>();
		elementShareQueryWrapper.lambda()
                .eq(ElementShare::getOrganizationId,elementShare.getOrganizationId())
                .eq(ElementShare::getElementId,elementShare.getElementId());
		//check share
		ElementShare currentE = elementShareMapper.selectOne(elementShareQueryWrapper);
		if (currentE == null) {
			throw new RayinBusinessException("抱歉，此构件已取消共享");
		}
	}
}

