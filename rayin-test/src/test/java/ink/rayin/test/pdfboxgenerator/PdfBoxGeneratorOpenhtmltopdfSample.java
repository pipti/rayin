package ink.rayin.test.pdfboxgenerator;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.base.Signature;
import ink.rayin.htmladapter.base.utils.JsonSchemaValidator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxSignature;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.sql.Timestamp;

/**
 * Openhtmltopdf适配生成器测试类
 * Openhtmltopdf adapter generator test class
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PdfBoxGeneratorOpenhtmltopdfSample {
    static PdfGenerator pdfGenerator;
    static  {
        try {
            pdfGenerator = new PdfBoxGenerator();
            pdfGenerator.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * fapiao
     * 发票样例生成测试
     * single element generate test
     */
    @Test
    public void fapiaoGenerateTest() throws Exception {
        log.info("fapiaoGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));
        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("samples/receipt/fapiao/data.json");
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
        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("samples/receipt/fapiao/element1.html"), jsonData, outputFile);

        log.info("fapiaoGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    
}
