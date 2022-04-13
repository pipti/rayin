
package ink.rayin.app.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import ink.rayin.app.web.dao.*;
import ink.rayin.app.web.exception.RayinBusinessException;
import ink.rayin.app.web.model.*;
import ink.rayin.app.web.oss.builder.OssBuilder;
import ink.rayin.app.web.oss.model.RayinFile;
import ink.rayin.app.web.service.IUserTemplateService;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.base.model.tplconfig.Element;
import ink.rayin.htmladapter.base.model.tplconfig.PageNumDisplayPos;
import ink.rayin.htmladapter.base.model.tplconfig.RayinMeta;
import ink.rayin.htmladapter.base.model.tplconfig.TemplateConfig;

import ink.rayin.tools.utils.BeanConvert;
import ink.rayin.tools.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * [模板管理服务类].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-03-02 Wang Zhu create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
@Service
@Slf4j
public class UserTemplateService implements IUserTemplateService {
	private static Logger logger = LoggerFactory.getLogger(UserTemplateService.class);
	@Resource
	private UserTemplateMapper userTemplateMapper;
	@Resource
	private UserTemplateElementMapper userTemplateElementMapper;
	@Resource
	private UserElementMapper userElementMapper;
	@Resource
	private PdfGenerator pdfCreateService;
	@Resource
	private UsersMapper usersMapper;
	@Resource
	private UserTemplateApproveMapper userTemplateApproveMapper;

	@Resource
	private UserOrganizationMapper userOrganizationMapper;
	@Resource
	private OrganizationMapper organizationMapper;
	@Resource
	private OssBuilder ossBuilder;
	/**
	 * 模板查询
	 *
	 * @param page
	 * @param ut
	 * @return
	 */
	@Override
	public IPage<UserTemplate> userTemplateQuery(IPage<UserTemplate> page, UserTemplate ut){
		IPage<UserTemplate> userTemplateList = null;
		if(StringUtils.isBlank(ut.getName())){
			userTemplateList = userTemplateMapper.selectTemplatePageNoCondition(page, ut);
		}else {
			userTemplateList = userTemplateMapper.selectTemplatePageByName(page, ut);
		}

		return userTemplateList;
	}

	/**
	 * 模板新增/更新
	 * @param ut
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userTemplateSave(UserTemplate ut) throws IllegalAccessException, IOException, InstantiationException {

		// 插入模板配置
		int i;
		if(StringUtils.isBlank(ut.getTemplateId())){
			Users users = usersMapper.selectById(ut.getUserId());
			ut.setUserName(users.getUsername());
			i = userTemplateMapper.insert(ut);
		}
		// 拼装TPL配置
		TemplateConfig tc = new TemplateConfig();
		tc.setTemplateId(ut.getTemplateId());
		tc.setElements(JSON.parseArray(ut.getElConfig(), Element.class));

		tc.setAuthor(ut.getAuthor());
		tc.setTitle(ut.getTitle());
		tc.setCreator(ut.getCreator());
		tc.setKeywords(ut.getKeywords());
		tc.setProducer(ut.getProducer());
		tc.setSubject(ut.getSubject());
		tc.setPassword(ut.getPassword());
		tc.setStartTime(ut.getPdfStartTimeStr());
		tc.setEndTime(ut.getPdfEndTimeStr());
		tc.setEditable(ut.isEditable());
		tc.setTemplateName(ut.getName());
		tc.setPageNumDisplayPoss(JSONObject.parseArray(ut.getPageNumDisplayPoss(), PageNumDisplayPos.class));
//		if(ut.getPageNumDisplayPoss() != null && ut.getPageNumDisplayPoss().size() > 0){
//			List<ink.rayin.htmladapter.base.model.tplconfig.PageNumDisplayPos> pdl =
//					new ArrayList<ink.rayin.htmladapter.base.model.tplconfig.PageNumDisplayPos>();
//
//			for(PageNumDisplayPos k:ut.getPageNumDisplayPoss()){
//				pdl.add(BeanConvert.convert(k, ink.rayin.htmladapter.base.model.tplconfig.PageNumDisplayPos.class));
//			}
//			tc.setPageNumDisplayPoss(pdl);
//		}
		ut.setTplConfig(JSONObject.toJSONString(tc));

		if(StringUtils.isNotBlank(ut.getAlias())){
			QueryWrapper qw = new QueryWrapper();
			qw.eq("organization_id",ut.getOrganizationId());
			qw.eq("alias",ut.getAlias());
			qw.notIn("template_id",ut.getTemplateId());
			if(userTemplateMapper.selectCount(qw) > 0){
				throw new RayinBusinessException("该项目\""+ut.getAlias()+"\"别名已存在，请选择其他别名！");
			}
		}


		UpdateWrapper uw = new UpdateWrapper();
		uw.eq("user_id",ut.getUserId());
		uw.eq("template_id",ut.getTemplateId());
		uw.eq("template_version",ut.getTemplateVersion());
		uw.eq("organization_id",ut.getOrganizationId());
		i = userTemplateMapper.update(ut,uw);

		UpdateWrapper<UserTemplateElement> qw = new UpdateWrapper<UserTemplateElement>();
//		qw.eq("user_id",ut.getUserId());
		qw.eq("template_id",ut.getTemplateId());
		qw.eq("template_version",ut.getTemplateVersion());
		uw.eq("organization_id",ut.getOrganizationId());
		userTemplateElementMapper.delete(qw);

        List<UserTemplateElement> tplEls = null;
        if(ut.getElConfig() != null){
			// 插入构件配置明细
            tplEls = JSON.parseArray(ut.getElConfig(),UserTemplateElement.class);
			int count = 0;
            for(UserTemplateElement tplEl:tplEls){
				tplEl.setTemplateId(ut.getTemplateId());
				tplEl.setTemplateVersion(ut.getTemplateVersion());
				tplEl.setOrganizationId(ut.getOrganizationId());
				tplEl.setSeq(count++ + "");
				userTemplateElementMapper.insert(tplEl);
			}

        }
		return i;
	}


	/**
	 * 模板逻辑删除
	 * @param ut
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userTemplateLogicalDel(UserTemplate ut) {
		return userTemplateMapper.update(null, Wrappers.<UserTemplate>update().lambda().set(UserTemplate::getDelFlag,"1")
				.eq(UserTemplate::getOrganizationId, ut.getOrganizationId())
				.eq(UserTemplate::getTemplateVersion,ut.getTemplateVersion())
				.eq(UserTemplate::getTemplateId,ut.getTemplateId()));
	}

	/**
	 * 模板物理删除
	 * @param ut
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userTemplateDel(UserTemplate ut) {

		UpdateWrapper<UserTemplate> qw = new UpdateWrapper<UserTemplate>();
		qw.eq("user_id",ut.getUserId()).eq("template_id",ut.getTemplateId())
		.eq("organization_id",ut.getOrganizationId());

		return userTemplateMapper.delete(qw);
	}
	/**
	 * 模板导出
	 * @param ut
	 * @return
	 */
	@Override
	public UserTemplate userTemplateOnlineExport(UserTemplate ut) {
		return userTemplateMapper.selectOne(Wrappers.<UserTemplate>query().lambda()
				.eq(UserTemplate::getOrganizationId,ut.getOrganizationId())
				.eq(UserTemplate::getTemplateId,ut.getTemplateId())
				.eq(UserTemplate::getTemplateVersion,ut.getTemplateVersion())
		);
	}


	/**
	 * 线下模板导出
	 * @param ut
	 * @return
	 */
	@Override
	public String userTemplateOfflineExport(UserTemplate ut) {

		UserTemplate userTemplate = userTemplateMapper.selectOne(Wrappers.<UserTemplate>query().lambda()
				.eq(UserTemplate::getOrganizationId,ut.getOrganizationId())
				.eq(UserTemplate::getTemplateId,ut.getTemplateId())
				.eq(UserTemplate::getTemplateVersion,ut.getTemplateVersion())
				.select(UserTemplate.class,i->i.getProperty().equals("tplConfig"))
		);
		return userTemplate.getTplConfig();
	}


	/**
	 * 模板导入
	 * 根据模板是否新建标记来是否作为新模板插入或更新原有模板
	 * 无论是新模板插入还是原有模板，连带的构件依据element_id判断是否存在，存在则更新，不存在则新插入
	 * @param ut
	 * @param newTemplate
	 * @return
	 */
	@Override
	public int userTemplateImport(String userId,String orgId, UserTemplate ut, boolean newTemplate) {
		boolean templateIsExist = false;
		Users users = usersMapper.selectById(userId);
		ut.setOrganizationId(orgId);

		if(StringUtils.isNotBlank(ut.getAlias())){
			QueryWrapper qw = new QueryWrapper();
			qw.eq("organization_id",orgId);
			qw.eq("alias",ut.getAlias());
			qw.notIn("templateId", ut.getTemplateId());
			if(userTemplateMapper.selectCount(qw) > 0){
				ut.setAlias(ut.getAlias() + "_1");
			}
		}


		if(newTemplate){
			ut.setTemplateId(null);
			userTemplateMapper.insert(ut);
		}else{
			UpdateWrapper<UserTemplate> uqw = new UpdateWrapper<UserTemplate>();
			uqw.eq("template_id",ut.getTemplateId()).eq("organization_id", orgId);
			ut.setUpdateTime(new Date());
			if(userTemplateMapper.update(ut, uqw) == 0){
				ut.setUserId(userId);
				ut.setUserName(users.getUsername());
				ut.setCreateTime(new Date());
				ut.setUpdateTime(null);
				userTemplateMapper.insert(ut);
			}else{
				templateIsExist = true;
			}
		}


		String elConfig = ut.getElConfig();
		List<UserTemplateElement> els = JSONArray.parseArray(elConfig,UserTemplateElement.class);
		UserElement uel;
		QueryWrapper<UserElement> qw ;
		UpdateWrapper<UserElement> qu ;

		UpdateWrapper<UserTemplateElement> uteQu = new UpdateWrapper<UserTemplateElement>();

		uteQu.eq("template_id", ut.getTemplateId());
		uteQu.eq("template_version", ut.getTemplateVersion());
		uteQu.eq("organization_id",orgId);
		userTemplateElementMapper.delete(uteQu);


		for(UserTemplateElement el:els){
			qw = new QueryWrapper<UserElement>();
			qu = new UpdateWrapper<UserElement>();
			uel = new UserElement();
			uel.setOrganizationId(orgId);
			//uel.setCreateTime(new Date());
			//uel.setUserId(userId);
			uel.setElementId(el.getElementId());
			uel.setElementThum(el.getElementThum());
			uel.setElementVersion(el.getElementVersion());
			//uel.setUserName(users.getUsername());
			uel.setContent(el.getContent());
			uel.setName(el.getName());
			uel.setMemo(el.getMemo());
			uel.setTestData(el.getTestData());
//			if(newElement){
//				uel.setElementId(null);
//				userElementMapper.insert(uel);
//			}else{
				//qw.eq("element_id",el.getElementId());
				//qw.eq("organization_id",ut.getOrganizationId());

				//if(userElementMapper.selectCount(qw) > 0){
			qu.eq("element_id",el.getElementId());
			qu.eq("organization_id",orgId);

			qw.eq("element_id",el.getElementId());
			qw.eq("organization_id",orgId);
			uel.setUpdateUserId(userId);
			uel.setUpdateUserName(users.getUsername());
			uel.setUpdateTime(new Date());
			uel.setUpdateUserName(users.getUsername());

			uel.setOrganizationId(null);
			uel.setElementId(null);
			if(userElementMapper.update(uel,qu) == 0){
				uel.setUpdateUserName(null);
				uel.setUpdateTime(null);
				uel.setUserId(userId);
				uel.setCreateTime(new Date());
				uel.setUserName(users.getUsername());

				uel.setOrganizationId(orgId);
				uel.setElementId(el.getElementId());
				userElementMapper.insert(uel);
			}

				//}
//			}
		}

		return 1;
	}





	/**
	 * 模板测试预览
	 * @param ut
	 * @return
	 */
	@Override
	public JSONObject userTemplateView(UserTemplate ut) throws Exception {
		UserTemplate utr = userTemplateMapper.selectOne(Wrappers.<UserTemplate>query().lambda().eq(UserTemplate::getUserId,ut.getUserId())
				.eq(UserTemplate::getOrganizationId,ut.getOrganizationId())
				.eq(UserTemplate::getTemplateId,ut.getTemplateId())
				.eq(UserTemplate::getTemplateVersion,ut.getTemplateVersion()));
		JSONObject r = new JSONObject();
		final Base64.Encoder encoder = Base64.getEncoder();
		long start = System.currentTimeMillis();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		r.put("pdfMetadata",pdfCreateService.generateEncryptPdfStreamByConfigStr(utr.getTplConfig(),JSON.parseObject(ut.getTestData()),baos,null));
		logger.debug("生成时间：" + (System.currentTimeMillis() - start)/1000 + "s");
		r.put("pdfFile",encoder.encodeToString(baos.toByteArray()));
		return r;
	}
	/**
	 * 模板测试插入存储
	 * @param ut
	 * @return
	 */
	@Override
	public JSONObject templateGenerate(UserTemplate ut) throws Exception{
		UserTemplate utr = userTemplateMapper.selectOne(Wrappers.<UserTemplate>query().lambda().eq(UserTemplate::getUserId,ut.getUserId())
				.eq(UserTemplate::getOrganizationId,ut.getOrganizationId())
				.eq(UserTemplate::getTemplateId,ut.getTemplateId())
				.eq(UserTemplate::getTemplateVersion,ut.getTemplateVersion()));
		JSONObject r = new JSONObject();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RayinMeta pdfMeta = pdfCreateService.generateEncryptPdfStreamByConfigStr(utr.getTplConfig(),JSON.parseObject(ut.getTestData()),baos,null);

		UserOrganization query = new UserOrganization();
		query.setOrganizationId(ut.getOrganizationId());
		query.setUserId(ut.getUserId());
		UserOrganization userOrg = userOrganizationMapper.userOrganizationQueryOne(query);
		if(StringUtils.isBlank(userOrg.getThirdStorageBucket())){
			throw new RayinBusinessException("请在项目中设置存储桶名称！");
		}
		RayinFile rayinFile = ossBuilder.template().putFile(userOrg.getThirdStorageBucket(), StringUtil.randomUUID() + ".pdf",new ByteArrayInputStream(baos.toByteArray()));
		return JSON.parseObject(JSON.toJSONString(rayinFile));
	}


	/**
	 * 模板测试数据保存
	 * @param ut
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userTemplateTestDataSave(UserTemplate ut) {
		return userTemplateMapper.update(null, Wrappers.<UserTemplate>update().lambda().set(UserTemplate::getTestData,ut.getTestData())
				.eq(UserTemplate::getUserId, ut.getUserId())
				.eq(UserTemplate::getTemplateVersion,ut.getTemplateVersion())
				.eq(UserTemplate::getTemplateId,ut.getTemplateId()));
	}

	/**
	 * 模板恢复
	 * @param uem
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userTemplateResume(UserTemplate uem){
		return userTemplateMapper.update(null,Wrappers.<UserTemplate>update().lambda().set(UserTemplate::getDelFlag,"0")
				.eq(UserTemplate::getUserId, uem.getUserId())
				.eq(UserTemplate::getTemplateVersion,uem.getTemplateVersion())
				.eq(UserTemplate::getTemplateId,uem.getTemplateId()));
	}

	/**
	 * 模板分享至其他项目
	 * @param parameter
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userTemplatePublishToOtherOrgPendingApproval(String userId, String orgId, UserTemplateApprove parameter) throws IllegalAccessException, IOException, InstantiationException {


		QueryWrapper utqw = new QueryWrapper();
		utqw.eq("organization_id",parameter.getOrganizationId());
		utqw.eq("template_id",parameter.getTemplateId());
		utqw.eq("template_version",parameter.getTemplateVersion());
		utqw.eq("del_flag", false);
		UserTemplate ut = userTemplateMapper.selectOne(utqw);
		if(ut == null){
			throw new RayinBusinessException("该模板无法找到，请确认是否已删除！");
		}

		QueryWrapper utaqw = new QueryWrapper();
		utaqw.eq("template_id",parameter.getTemplateId());
		utaqw.eq("template_version",parameter.getTemplateVersion());
		utaqw.eq("organization_id",parameter.getOrganizationId());
		utaqw.eq("to_organization_id",parameter.getToOrganizationId());

		utaqw.eq("approve_status","wait");
		if(userTemplateApproveMapper.selectCount(utaqw) > 0){
			throw new RayinBusinessException("该模板正在等待同步审核，无需再次发起申请！");
		}
		Users users = usersMapper.selectById(userId);
		UserTemplateApprove utai = BeanConvert.convert(ut,UserTemplateApprove.class);
		utai.setApproveStatus("wait");
		utai.setSubmitUserId(userId);
		utai.setSubmitTime(new Date());
		utai.setSubmitUserName(users.getUsername());
		utai.setToOrganizationId(parameter.getToOrganizationId());
		utai.setNewOne(parameter.isNewOne());

		Organization toOrg = organizationMapper.selectById(parameter.getToOrganizationId());
		//通知管理员
		List<UserOrganization> list = userOrganizationMapper.selectList(
				new QueryWrapper<UserOrganization>().lambda()
						.eq(UserOrganization::getOrganizationId,parameter.getToOrganizationId())
					.lt(UserOrganization::getRoleId,2)
		);
		list.stream().forEach(userOrganization -> {
			Message message = new Message();
			message.setUserId(userOrganization.getUserId());
			message.setInfo("项目：<"+toOrg.getOrganizationName()+">中，您有一条模板审核待处理");
			message.setUrl("");
		});
		return userTemplateApproveMapper.insert(utai);
	}

	@Override
	public IPage<UserTemplateApprove> userTemplateApproveWaitQuery(IPage<UserTemplateApprove> page, UserTemplateApprove uea){
		IPage<UserTemplateApprove> userTemplateApproveList = null;
		uea.setApproveStatus("wait");
		if(StringUtils.isBlank(uea.getName())){
			userTemplateApproveList = userTemplateApproveMapper.selectTemplateApprovePageNoCondition(page, uea);
		}else {
			userTemplateApproveList = userTemplateApproveMapper.selectTemplateApprovePageByName(page, uea);
		}
		return userTemplateApproveList;
	}

	@Override
	public IPage<UserTemplateApprove> userTemplateApproveFinishQuery(IPage<UserTemplateApprove> page, UserTemplateApprove uea){
		IPage<UserTemplateApprove> userTemplateApproveList = null;
		if(StringUtils.isBlank(uea.getName())){
			userTemplateApproveList = userTemplateApproveMapper.selectTemplateApproveFinishPageNoCondition(page, uea);
		}else {
			userTemplateApproveList = userTemplateApproveMapper.selectTemplateApproveFinishPageByName(page, uea);
		}
		return userTemplateApproveList;
	}



	/**
	 * 模板测试
	 * @param utap
	 * @return
	 */
	@Override
	public JSONObject userTemplateApprovePdfView(UserTemplateApprove utap) throws Exception {
		UserTemplateApprove uta = userTemplateApproveMapper.selectById(utap.getId());
		JSONObject r = new JSONObject();
		final Base64.Encoder encoder = Base64.getEncoder();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		r.put("pdfMetadata",pdfCreateService.generateEncryptPdfStreamByConfigStr(uta.getTplConfig(),JSON.parseObject(uta.getTestData()),baos,null));
		r.put("pdfFile",encoder.encodeToString(baos.toByteArray()));
		return r;
	}


	/**
	 * 模板审批通过
	 * @param userId
	 * @param orgId
	 * @param parameter
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int userTemplateApproveCheck(String userId,String orgId,UserTemplateApprove parameter) throws IllegalAccessException, IOException, InstantiationException {
		String templateId;
		String templateVersion;
		UserTemplateApprove uua = new UserTemplateApprove();
		uua.setApproveIdea(parameter.getApproveIdea());
		uua.setApproveStatus(parameter.getApproveStatus());
		uua.setId(parameter.getId());
		int updateCount =  userTemplateApproveMapper.updateById(uua);
		if(!parameter.getApproveStatus().equals("accept") && !parameter.getApproveStatus().equals("reject")){
			throw new RayinBusinessException("请选择审批意见！");
		}
		if(!parameter.getApproveStatus().equals("accept")){
			Message message = new Message();
			message.setUserId(parameter.getSubmitUserId());
			message.setInfo("您的模板同步申请被拒绝");
			return updateCount;
		}
		// 审核通过流程
		UserTemplateApprove uta = userTemplateApproveMapper.selectById(parameter.getId());
		UserTemplate ut;
		ut = BeanConvert.convert(uta,UserTemplate.class);
		//如果不覆盖，新增
		if(uta.isNewOne() == true){
			ut.setOrganizationId(uta.getToOrganizationId());
			ut.setCreateTime(new Date());
			ut.setUserId(uta.getSubmitUserId());
			ut.setUserName(uta.getSubmitUserName());
			ut.setUpdateTime(null);

			// 重新组装elconfig
			List<UserTemplateElement> ule = JSONObject.parseArray(ut.getElConfig(), UserTemplateElement.class);
			for(int i = 0;i < ule.size(); i++){
				UserTemplateElement utlei = ule.get(i);
					utlei.setOrganizationId(uta.getToOrganizationId());
					ule.set(i,utlei);
			}
			ut.setElConfig(JSONObject.toJSONString(ule));

			userTemplateMapper.insert(ut);
			templateId = ut.getTemplateId();
			templateVersion = "1.0";

		}else{
			//如果覆盖
			QueryWrapper utqw = new QueryWrapper();
			utqw.eq("template_id",uta.getTemplateId());
			utqw.eq("template_version",uta.getTemplateVersion());
			utqw.eq("organization_id",uta.getToOrganizationId());

			UserTemplate userTemplateSelectOne = userTemplateMapper.selectOne(utqw);
			//检查是否存在
			if(userTemplateSelectOne == null){
				//不存在，新增
				ut.setOrganizationId(uta.getToOrganizationId());
				ut.setCreateTime(new Date());
				ut.setUserId(uta.getSubmitUserId());
				ut.setUserName(uta.getSubmitUserName());
				ut.setUpdateTime(null);
				// 重新组装elconfig
				List<UserTemplateElement> ule = JSONObject.parseArray(ut.getElConfig(), UserTemplateElement.class);
				for(int i = 0;i < ule.size(); i++){
					UserTemplateElement utlei = ule.get(i);
					utlei.setOrganizationId(uta.getToOrganizationId());
					ule.set(i,utlei);
				}
				ut.setElConfig(JSONObject.toJSONString(ule));
				userTemplateMapper.insert(ut);
				templateId = ut.getTemplateId();
				templateVersion = "1.0";
			}else{
				//存在，更新
				QueryWrapper<UserTemplate> upqw = new QueryWrapper<UserTemplate>();
				upqw.eq("template_id",uta.getTemplateId());
				upqw.eq("template_version",uta.getTemplateVersion());
				upqw.eq("organization_id",uta.getToOrganizationId());
				ut.setUpdateTime(new Date());

				// 重新组装elconfig
				List<UserTemplateElement> ule = JSONObject.parseArray(ut.getElConfig(), UserTemplateElement.class);
				for(int i = 0;i < ule.size(); i++){
					UserTemplateElement utlei = ule.get(i);
					utlei.setOrganizationId(uta.getToOrganizationId());
					ule.set(i,utlei);
				}
				templateId = ut.getTemplateId();
				templateVersion = ut.getTemplateVersion();

				ut.setElConfig(JSONObject.toJSONString(ule));
				ut.setOrganizationId(null);
				ut.setTemplateVersion(null);
				ut.setTemplateId(null);
				ut.setDelFlag(false);
				userTemplateMapper.update(ut,upqw);

			}
		}
		//无论是否为新增构件都进行更新操作
		String elConfig = ut.getElConfig();
		List<UserTemplateElement> els = JSONArray.parseArray(elConfig,UserTemplateElement.class);
		UserElement uel;
		UserTemplateElement ute;
		QueryWrapper<UserElement> qw ;
		UpdateWrapper<UserElement> qu ;
		UpdateWrapper<UserTemplateElement> uteQu = new UpdateWrapper<UserTemplateElement>();

		uteQu.eq("template_id", templateId);
		uteQu.eq("template_version", templateVersion);
		uteQu.eq("organization_id",uta.getToOrganizationId());
		userTemplateElementMapper.delete(uteQu);

		for(UserTemplateElement el:els){
			qw = new QueryWrapper<UserElement>();
			qu = new UpdateWrapper<UserElement>();
			uel = new UserElement();
			uel.setOrganizationId(uta.getToOrganizationId());
			uel.setElementId(el.getElementId());
			uel.setElementThum(el.getElementThum());
			uel.setElementVersion(el.getElementVersion());
			uel.setContent(el.getContent());
			uel.setName(el.getName());
			uel.setMemo(el.getMemo());
			uel.setTestData(el.getTestData());
			uel.setUserId(uta.getSubmitUserId());
			uel.setUserName(uta.getSubmitUserName());
			//更新条件
			qu.eq("element_id",uel.getElementId());
			qu.eq("organization_id",uel.getOrganizationId());

			//查询条件
			qw.eq("element_id",uel.getElementId());
			qw.eq("organization_id",uel.getOrganizationId());
			uel.setUpdateUserId(uta.getSubmitUserId());
			uel.setUpdateUserName(uta.getSubmitUserName());
			uel.setUpdateTime(new Date());

			//int i= userElementMapper.selectCount(qu);
			//int i= userElementMapper.update(uel,qu);
			if(userElementMapper.selectCount(qw) == 0){
				uel.setUpdateUserName(null);
				uel.setUpdateTime(null);
				uel.setCreateTime(new Date());
				userElementMapper.insert(uel);
			}else{
				uel.setElementId(null);
				uel.setOrganizationId(null);
				userElementMapper.update(uel,qu);
			}
			el.setTemplateId(templateId);
			el.setTemplateVersion(templateVersion);

			userTemplateElementMapper.insert(el);
		}
		return 1;
	}
}
