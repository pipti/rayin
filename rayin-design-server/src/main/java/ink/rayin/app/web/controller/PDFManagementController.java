package ink.rayin.app.web.controller;

import com.alibaba.fastjson.JSONObject;
import ink.rayin.app.web.exception.RayinBusinessException;
import ink.rayin.app.web.model.RestResponse;

import ink.rayin.app.web.model.UserOrganization;
import ink.rayin.app.web.annotation.OrgId;
import ink.rayin.app.web.annotation.UserId;
import ink.rayin.app.web.oss.builder.OssBuilder;
import ink.rayin.app.web.oss.model.RayinFile;
import ink.rayin.app.web.oss.model.RayinFiles;
import ink.rayin.app.web.service.impl.UserOrganizationService;
import ink.rayin.tools.utils.ResourceUtil;
import ink.rayin.tools.utils.StringPool;
import ink.rayin.tools.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * [PDF文件管理Controller].
 * [PDF文件管理 WebApp Rest API]
 * <h3>version info：</h3><br>
 * v1.0 2020-04-03 Wang Zhu create<br>
 * <br>
 * @author Jonah Wang
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
@RestController
public class PDFManagementController {
    private static Logger logger = LoggerFactory.getLogger(PDFManagementController.class);

    @Resource
    private UserOrganizationService userOrganizationService;

    @Resource
    OssBuilder ossBuilder;
    @Value("${rayin.storage.url}")
    String storageUrl;
    /**
     * PDF文件查询
     *
     * @param orgId
     * @param userId
     * @return
     */
    @PostMapping(value = {"/organization/pdf/query"})
    public RestResponse organizationPDFQuery(@OrgId String orgId,
                                             @UserId String userId,
                                             @RequestBody JSONObject params,
                                             @RequestParam Integer pageCurrent,
                                             @RequestParam Integer pageSize,
                                             @RequestParam String prefix) {

        UserOrganization uo = userOrganizationService.userOrganizationQueryOne(new UserOrganization().setUserId(userId).setOrganizationId(orgId));
        if(uo == null){
            throw new RayinBusinessException("您无权访问该项目的文件内容！");
        }
        if(StringUtil.isBlank(uo.getThirdStorageBucket())){
            throw new RayinBusinessException("请在项目中设置存储桶名称！");
        }

        RayinFiles rayinFiles = ossBuilder.template().getFileList(uo.getThirdStorageBucket(),uo.getOrganizationId() + StringPool.SLASH + prefix);

        rayinFiles.setPrefix(rayinFiles.getPrefix().substring(uo.getOrganizationId().length() + 1));
        return RestResponse.success(rayinFiles);
    }

    /**
     * PDF文件预览
     *
     * @param orgId
     * @param userId
     * @param jsonObject
     * @return
     */
    @ApiOperation(value="PDF预览", notes="PDF预览", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orgId", value = "项目ID", required = true,
                    dataType = "String", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true,
                    dataType = "String", paramType = "query",defaultValue = ""),
            @ApiImplicitParam(name = "jsonObject", value = "文件属性", required = true,
                    dataType = "JSONObject", paramType = "body",defaultValue = "")
    })
    @PostMapping(value = {"/organization/pdf/view"})
    public RestResponse organizationPDFView(@OrgId String orgId,
              @UserId String userId, @RequestBody(required = true) JSONObject jsonObject) throws IOException {
        String url = jsonObject.getString("url");

        ByteArrayOutputStream bos = ResourceUtil.getResourceAsByte(url);

        JSONObject r = new JSONObject();
        final Base64.Encoder encoder = Base64.getEncoder();

        String res = encoder.encodeToString(bos.toByteArray());
        r.put("pdfFile",res);

        return RestResponse.success(r);
    }


    @PostMapping(value = {"/organization/pdf/download"})
    public void organizationPDFDownload(@OrgId String orgId,
                                            @UserId String userId,
                                           @RequestBody(required = true) RayinFile rayinFile,
                                           HttpServletResponse httpServletResponse) throws IOException {
        UserOrganization uo = userOrganizationService.userOrganizationQueryOne(new UserOrganization().setUserId(userId).setOrganizationId(orgId));

        if(uo == null){
            throw new RayinBusinessException("您无权访问该项目的文件内容！");
        }
        String url = rayinFile.getPresignedLink();

        ByteArrayOutputStream byteOs = ResourceUtil.getResourceAsByte(url);

        httpServletResponse.setContentType("application/pdf;charset=utf-8");
        httpServletResponse.setHeader("Content-disposition", "attachment; filename=" + rayinFile.getName().substring(0, rayinFile.getName().length() - 1));
        httpServletResponse.setHeader("Content-Length", String.valueOf(byteOs.toByteArray().length));

        OutputStream stream = httpServletResponse.getOutputStream();
        stream.write(byteOs.toByteArray());
        stream.flush();
        stream.close();
    }
}
