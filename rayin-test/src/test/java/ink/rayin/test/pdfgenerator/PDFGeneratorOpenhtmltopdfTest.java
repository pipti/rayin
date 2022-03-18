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
public class PDFGeneratorOpenhtmltopdfTest {
    PDFGeneratorInterface pdfGenerator = new PDFGenerator();

    public PDFGeneratorOpenhtmltopdfTest() throws Exception {
        pdfGenerator.init();
    }

    /**
     * example1
     * 单个构件生成测试
     * single element generate test
     */
    @Test
    public void elementGenerateTest() throws Exception {
        log.info("elementGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String outputFile ="";
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        outputFile = (outputFile == null || outputFile.equals(""))? new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example1_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

        //数据参数可以为空
        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("example/element/example1/element1.html"),null,outputFile);

        log.info("elementGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example2
     * 简单的模板生成测试
     * simple template generate test
     */
    @Test
    public void simpleTemplateGenerateTest() throws Exception {
        log.info("simpleTemplateGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String outputFile ="";
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        outputFile = (outputFile == null || outputFile.equals(""))? new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example2_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("example/element/example2/tpl.json"),null,outputFile);

        log.info("simpleTemplateGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example3
     * 单个构件绑定数据生成测试
     * single element bind data generate test
     */
    @Test
    public void elementBindDataGenerateTest() throws Exception {
        log.info("elementBindDataGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("example/element/example3/data.json");
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
                + "example3_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("example/element/example3/element1.html"),jsonData,outputFile);

        log.info("elementBindDataGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example4 绑定数据的模板生成测试
     * template bind data generate test
     */
    @Test
    public void templateBindDataGenerateTest() throws Exception {
        log.info("templateBindDataGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        //String jsonFileName = "card.json";
        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("example/element/example4/data.json");
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

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("example/element/example4/tpl.json"),jsonData,outputFile);

        log.info("templateBindDataGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example5
     * 复杂构件绑定数据生成测试
     * single element bind data generate test
     */
    @Test
    public void complexElementBindDataGenerateTest() throws Exception {
        log.info("elementBindDataGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("example/element/example5/data.json");
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
                + "example5_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("example/element/example5/element1.html"),jsonData,outputFile);

        log.info("elementBindDataGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example6
     * 复杂模板配置生成测试
     * complex template generate test
     */
    @Test
    public void complexTemplateGenerateTest() throws Exception {
        log.info("complexTemplateGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("example/element/example6/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据构建配置生成PDF
        JSONObject jsonData = (JSONObject)JSONObject.parse(jsonDataNode.toString());

        String outputFile ="";
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        outputFile = (outputFile == null || outputFile.equals(""))? new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example6_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("example/element/example6/tpl.json"),jsonData,outputFile);

        log.info("complexTemplateGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example7
     * 特殊标签测试
     * special tag generate test
     */
    @Test
    public void specialTagGenerateTest() throws Exception {
        log.info("specialTagGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("example/element/example7/data.json");
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
                + "example7_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("example/element/example7/tpl.json"),jsonData,outputFile);

        log.info("specialTagGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }


    /**
     * example8
     * 获取生成信息
     * get page info test
     */
    @Test
    public void getPageInfoTest() throws Exception {
        log.info("getPageInfoTest start time：" + new Timestamp(System.currentTimeMillis()));
        String outputFile ="";
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        outputFile = (outputFile == null || outputFile.equals(""))? new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example8_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("example/element/example8/tpl.json"), null, outputFile);

        log.info(pdfGenerator.pdfPageInfoRead(ResourceUtil.getResourceAsStream(outputFile)));

        log.info("getPageInfoTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

}
