package ink.rayin.test.pdfboxgenerator;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.houbb.junitperf.core.annotation.JunitPerfConfig;
import com.github.houbb.junitperf.core.rule.JunitPerfRule;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.base.Signature;
import ink.rayin.htmladapter.base.utils.JsonSchemaValidator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxSignature;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.sql.Timestamp;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PdfBoxGeneratorPerformanceTest {
    @Rule
    public JunitPerfRule junitPerfRule = new JunitPerfRule();

    static PdfGenerator pdfGenerator;
    Signature pdfSign = new PdfBoxSignature();

    static{
        try {
            pdfGenerator = new PdfBoxGenerator();
            pdfGenerator.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 10线程，执行 15000ms，默认以 html 输出测试结果
     */
    @Test
    @JunitPerfConfig(duration = 65000,threads = 20,warmUp = 5000)
    public void exp4TemplateBindDataGenerateTest() throws Exception {
        log.info("exp4TemplateBindDataGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        //String jsonFileName = "card.json";
        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example4/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据构建配置生成PDF
        JSONObject jsonData = (JSONObject)JSONObject.parse(jsonDataNode.toString());


        String outputFile ="";
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        outputFile = (outputFile == null || outputFile.equals(""))? new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example4_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example4/tpl.json"),jsonData,outputFile);

        log.info("exp4TemplateBindDataGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

}
