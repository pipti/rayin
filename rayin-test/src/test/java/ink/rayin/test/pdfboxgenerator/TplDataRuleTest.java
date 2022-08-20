package ink.rayin.test.pdfboxgenerator;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import ink.rayin.datarule.DataRule;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
public class TplDataRuleTest {
    static PdfGenerator pdfGenerator;
    static DataRule dataRule;
    static  {
        try {
            pdfGenerator = new PdfBoxGenerator();
            pdfGenerator.init();
            dataRule = new DataRule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据数据拼接脚本文件名
     * @throws Exception
     */
    @Test
    public void ruleScriptFileNameByDataComposeTest() throws Exception {
        JSONObject jsonData = new JSONObject();
        JSONPath.set(jsonData, "public.orgId" ,"110");
        JSONPath.set(jsonData, "public.prdCode" ,"PDA01");

        JSONObject otherData = new JSONObject();
        HashMap orgs = new HashMap();
        orgs.put("110", "北京分公司");

        JSONPath.set(otherData,"orgs", orgs);
        log.debug(otherData.toString());

        String scriptFileName = dataRule.getGroovyFileNameByData(jsonData, "_", "public.orgId", "public.prdCode");
        Object result = dataRule.executeGroovyFile(jsonData, otherData,"input", "other",
        "rules" + File.separator + scriptFileName);
        log.debug(result.toString());
    }

    /**
     * 根据数据规则脚本生成模板配置
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Test
    public void ruleScriptDynamicJointTemplateTest() throws IOException, InstantiationException, IllegalAccessException {
        JSONObject jsonData = new JSONObject();
        // 可以更换orgId = 110 或 orgId = 120，对应生成pdf也不同
        String orgId = "110";
        JSONPath.set(jsonData, "public.orgId" , orgId);
        JSONPath.set(jsonData, "public.prdCode" ,"PDA01");

        JSONObject otherData = new JSONObject();
        HashMap orgs = new HashMap();
        orgs.put("110", "北京分公司");

        JSONPath.set(otherData,"orgs", orgs);
        log.debug(otherData.toString());

        Object result = dataRule.executeGroovyFile(jsonData, otherData,"input", "other",
                "rules/dynamic_jonit_tpl.groovy");
        log.debug(JSONObject.toJSONString(result));
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "rule_script_dynamic_joint_tpl_test_orgid_" + orgId + "_openhtmltopdf_" + System.currentTimeMillis() + ".pdf";
        pdfGenerator.generatePdfFileByTplConfigStr(JSONObject.toJSONString(result), jsonData, outputFile);
    }

}
