package ink.rayin.test.pdfboxgenerator;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import ink.rayin.datarule.RayinDataRule;
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
    static RayinDataRule rayinDataRule;
    static  {
        try {
            pdfGenerator = new PdfBoxGenerator();
            pdfGenerator.init();
            rayinDataRule = new RayinDataRule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//
//    /**
//     * 根据数据编号指定对应的规则配置
//     * @param data json数据，用于生成PDF数据
//     * @param otherData json数据，辅助数据，例如一些用于判断的参数类数据
//     * @param dataName 脚本中的数据名称，例如input，则在脚本中可以直接使用input获取数据
//     * @param otherDataName 脚本中的其他数据名称
//     * @param scriptFileNameSeparator 文件数据名称分隔符，可空，例如
//     * @param scriptFileNameDataPaths 规则脚本文件名称对应的数据路径，可以指定多个，多个可使用第二个参数做拼接
//     *                            例如：public.org = 110 public.prdCode = PDA01 ,则参数(data, "_", "public.org","public.prdCode"),
//     *                            查找对应的规则为脚本名称为 "110_PDA01.ql"
//     * @throws Exception
//     */
//    public static Object ruleParse(JSONObject data,JSONObject otherData, String dataName,
//                                 String otherDataName, String scriptFileNameSeparator, String... scriptFileNameDataPaths) throws Exception {
//        if(data == null){
//            return null;
//        }
//        String script = "";
//        String qlScriptName = "";
//        for(String sndp : scriptFileNameDataPaths){
//            if(JSONPath.eval(data, sndp) == null){
//                throw new RayinException("parameter error,'" + sndp + "' data path not found!");
//            }
//            qlScriptName += JSONPath.eval(data, sndp).toString();
//            qlScriptName = StringUtils.isNotBlank(scriptFileNameSeparator)? qlScriptName + scriptFileNameSeparator : qlScriptName;
//        }
//        qlScriptName = StringUtils.isNotBlank(scriptFileNameSeparator) ? qlScriptName.substring(0, qlScriptName.length() - 1) : qlScriptName;
//        log.info("查找规则脚本文件：" + qlScriptName + ".ql");
//
//        if(StringUtils.isNotBlank(qlScriptName)){
//            script += ResourceUtil.getResourceAsString("rules/" + qlScriptName + ".ql", StandardCharsets.UTF_8);
//        }else{
//            log.error("data error,ql script name is null");
//            throw new RayinException("data error,ql script name is null");
//        }
//
//        IExpressContext<String, Object> expressContext = new DefaultContext<>();
//
//        if(StringUtils.isBlank(dataName)){
//            expressContext.put("inputData", data);
//        }else{
//            expressContext.put(dataName, data);
//        }
//
//        if(otherData != null) {
//            if (StringUtils.isBlank(otherDataName)) {
//                expressContext.put("otherData", otherData);
//            } else {
//                expressContext.put(otherDataName, otherData);
//            }
//        }
//
//
//        // ExpressRunner runner = new ExpressRunner();
//        ExpressRunner runner = new ExpressRunner(false, false);
//        //for (String exp : expList) {
//        Assert.assertTrue(runner.checkSyntax(script));
//        // }
//        //for (String exp : expList) {
//        ArrayList<String> mockClasses = new ArrayList<>();
//        Assert.assertTrue(runner.checkSyntax(script, true, mockClasses));
//        System.out.println("未识别的java类列表:" + mockClasses);
//        // }
//        List errorList = new ArrayList<>();
//        Object r = runner.execute(script, expressContext, errorList, true, true);
//        log.debug(r.toString());
//        //log.debug(errorList);
//        //Assert.assertEquals("操作符执行错误", "小强", r);
//        return r;
//    }

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

//        TplDataRuleTest.ruleParse(jsonData, otherData,"input", "",
//                "_", "public.orgId", "public.prdCode");

        Object result = rayinDataRule.executeGroovyFile(jsonData, otherData,"input", "other",
        "_", "rules", "public.orgId", "public.prdCode");
        log.debug(result.toString());
    }

    @Test
    public void ruleScriptDynamicJointTemplateTest() throws IOException, InstantiationException, IllegalAccessException {
        JSONObject jsonData = new JSONObject();
        // 可以更换value = 110 或 value = 120，对应生成pdf也不同
        JSONPath.set(jsonData, "public.orgId" ,"120");
        JSONPath.set(jsonData, "public.prdCode" ,"PDA01");

        JSONObject otherData = new JSONObject();
        HashMap orgs = new HashMap();
        orgs.put("110", "北京分公司");

        JSONPath.set(otherData,"orgs", orgs);
        log.debug(otherData.toString());

        Object result = rayinDataRule.executeGroovyFile(jsonData, otherData,"input", "other",
                "rules/DynamicJonitTpl.groovy");
        log.debug(JSONObject.toJSONString(result));
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "ruleScriptDynamicJointTemplateTest_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";
        pdfGenerator.generatePdfFileByTplConfigStr(JSONObject.toJSONString(result), jsonData, outputFile);
    }

}
