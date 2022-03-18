package ink.rayin.test.pdfgenerator;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import ink.rayin.htmladapter.base.PDFGeneratorInterface;
import ink.rayin.htmladapter.base.utils.JsonSchemaValidator;
import ink.rayin.htmladapter.openhtmltopdf.service.PDFGenerator;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.sql.Timestamp;

/**
 * Openhtmltopdf适配生成器测试类
 * Openhtmltopdf adapter generator test class
 */
@Slf4j
public class PDFGeneratorOpenhtmltopdfSample {
    PDFGeneratorInterface pdfGenerator = new PDFGenerator();

    public PDFGeneratorOpenhtmltopdfSample() throws Exception {
        pdfGenerator.init();
    }

    /**
     * fapiao
     * 发票样例生成测试
     * single element generate test
     */
    @Test
    public void fapiaoGenerateTest() throws Exception {
        log.info("fapiaoGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));
        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("sample/receipt/fapiao/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据单个构建配置生成PDF
        //generate pdf by element
        JSONObject jsonData = (JSONObject)JSONObject.parse(jsonDataNode.toString());

        String outputFile ="";
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        outputFile = (outputFile == null || outputFile.equals(""))? new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "fapiao_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

        //数据参数可以为空
        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("sample/receipt/fapiao/element1.html"), jsonData, outputFile);

        log.info("fapiaoGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    
}
