package ink.rayin.test.pdfboxgenerator;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.houbb.junitperf.core.annotation.JunitPerfConfig;
import com.github.houbb.junitperf.core.report.impl.ConsoleReporter;
import com.github.houbb.junitperf.core.report.impl.HtmlReporter;
import ink.rayin.datarule.DataRule;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.base.Signature;
import ink.rayin.htmladapter.base.utils.JsonSchemaValidator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxSignature;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.io.IOException;

/**
 * 简单的性能测试类
 */
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PdfBoxGeneratorPerformanceTest {

    static PdfGenerator pdfGenerator;
    static DataRule dataRule;
    Signature pdfSign = new PdfBoxSignature();

    static{
        try {
            pdfGenerator = new PdfBoxGenerator();
            pdfGenerator.init();
            dataRule = new DataRule();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 5线程，执行 30000ms，延迟1s执行，默认以 html 输出测试结果
     *
     * 备注：junitperf原始包模板数据显示格式存在问题，不显示图表，lib下为修改后的包
     */
    @Test
    @JunitPerfConfig(duration = 30_000,threads = 5,warmUp = 1_000,
            reporter = {HtmlReporter.class, ConsoleReporter.class})
    public void exp4TemplateBindDataGenerateTest() throws Exception {
        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example4/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据构建配置生成PDF
        JSONObject jsonData = JSONObject.parseObject(jsonDataNode.toString());


        String outputFile ="";
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("./");

        // 生成pdf路径
        outputFile = (outputFile == null || outputFile.equals(""))? new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example4_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

        StopWatch watch = StopWatch.createStarted();
        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example4/tpl.json"),jsonData,outputFile);
        watch.stop();

        log.info("exp4TemplateBindDataGenerateTest duration：" +  watch.getTime() + "ms");
    }


    /**
     * 模板测试
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Test
    @JunitPerfConfig(duration = 30_000,threads = 1,warmUp = 1_000,
            reporter = {HtmlReporter.class, ConsoleReporter.class})
    public void ruleScriptDynamicJointTemplateTest() throws IOException, InstantiationException, IllegalAccessException {
        JSONObject jsonData = new JSONObject();
        // 可以更换value = 110 或 value = 120，对应生成pdf也不同
        JSONPath.set(jsonData, "public.orgId" ,"120");
        JSONPath.set(jsonData, "public.prdCode" ,"PDA01");

        JSONObject otherData = new JSONObject();

        StopWatch watch = StopWatch.createStarted();
        Object result = dataRule.executeGroovyFile(jsonData, otherData,"input", "other",
                "rules/dynamic_jonit_tpl.groovy");
        watch.stop();
        log.info("ruleScriptDynamicJointTemplateTest duration：" +  watch.getTime() + "ms");
        log.debug(JSONObject.toJSONString(result));
//        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");
//        String outputFile = new File(outputFileClass)
//                .getParentFile().getParent()
//                + "/tmp/"
//                + "ruleScriptDynamicJointTemplateTest_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";
//        pdfGenerator.generatePdfFileByTplConfigStr(JSONObject.toJSONString(result), jsonData, outputFile);
    }

}
