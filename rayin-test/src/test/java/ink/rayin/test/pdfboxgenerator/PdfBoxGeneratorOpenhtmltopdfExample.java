package ink.rayin.test.pdfboxgenerator;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.base.Signature;
import ink.rayin.htmladapter.base.model.tplconfig.MarkInfo;
import ink.rayin.htmladapter.base.model.tplconfig.RayinMeta;
import ink.rayin.htmladapter.base.model.tplconfig.SignatureProperty;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Openhtmltopdf适配生成器测试类
 * Openhtmltopdf adapter generator test class
 */

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PdfBoxGeneratorOpenhtmltopdfExample {
    static PdfGenerator pdfGenerator;
    Signature pdfSign = new PdfBoxSignature();
    static  {
        try {
            pdfGenerator = new PdfBoxGenerator();
            pdfGenerator.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * example01
     * 单个构件生成测试
     * single element generate test
     */
    @Test
    public void exp01ElementGenerateTest() throws Exception {
        log.info("exp01ElementGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example01_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        //数据参数可以为空
        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example1/element1.html"),null,outputFile);

        log.info("exp01ElementGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example02
     * 简单的模板生成测试
     * simple template generate test
     */
    @Test
    public void exp02SimpleTemplateGenerateTest() throws Exception {
        log.info("exp02SimpleTemplateGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example02_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example2/tpl.json"),null,outputFile);

        log.info("exp02SimpleTemplateGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example03
     * 单个构件绑定数据生成测试
     * single element bind data generate test
     */
    @Test
    public void exp03ElementBindDataGenerateTest() throws Exception {
        log.info("exp03ElementBindDataGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example3/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据单个构建配置生成PDF
        //generate pdf by element
        JSONObject jsonData = (JSONObject)JSONObject.parse(jsonDataNode.toString());

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example03_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example3/element1.html"),jsonData,outputFile);

        log.info("exp03ElementBindDataGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example04 绑定数据的模板生成测试
     * template bind data generate test
     */
    @Test
    public void exp04TemplateBindDataGenerateTest() throws Exception {
        log.info("exp04TemplateBindDataGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        //String jsonFileName = "card.json";
        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example4/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据构建配置生成PDF
        JSONObject jsonData = (JSONObject)JSONObject.parse(jsonDataNode.toString());

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example04_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example4/tpl.json"),jsonData,outputFile);

        log.info("exp04TemplateBindDataGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example05
     * 复杂构件绑定数据生成测试
     * single element bind data generate test
     */
    @Test
    public void exp05ComplexElementBindDataGenerateTest() throws Exception {
        log.info("exp5ComplexElementBindDataGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example5/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据单个构建配置生成PDF
        //generate pdf by element
        JSONObject jsonData = (JSONObject)JSONObject.parse(jsonDataNode.toString());

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example05_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example5/element1.html"),jsonData,outputFile);

        log.info("exp05ComplexElementBindDataGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example06
     * 复杂模板配置生成测试
     * complex template generate test
     */
    @Test
    public void exp06ComplexTemplateGenerateTest() throws Exception {
        log.info("exp06ComplexTemplateGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example6/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据构建配置生成PDF
        JSONObject jsonData = (JSONObject)JSONObject.parse(jsonDataNode.toString());

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example06_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example6/tpl.json"),jsonData,outputFile);

        log.info("exp06ComplexTemplateGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example07
     * 特殊标签测试
     * special tag generate test
     */
    @Test
    public void exp07SpecialTagGenerateTest() throws Exception {
        log.info("exp07SpecialTagGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example7/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据单个构建配置生成PDF
        //generate pdf by element
        JSONObject jsonData = (JSONObject)JSONObject.parse(jsonDataNode.toString());

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example07_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example7/tpl.json"),jsonData,outputFile);

        log.info("exp07SpecialTagGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }


    /**
     * example08
     * 获取生成信息
     * get page info test
     */
    @Test
    public void exp08GetPageInfoTest() throws Exception {
        log.info("exp08GetPageInfoTest start time：" + new Timestamp(System.currentTimeMillis()));

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example08_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example8/tpl.json"), null, outputFile);

        log.info(pdfGenerator.pdfPageInfoRead(ResourceUtil.getResourceAsStream(outputFile)));

        log.info("exp08GetPageInfoTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example9
     * 签章
     * get page info test
     */
    @Test
    public void exp09SignTest() throws Exception {
        log.info("exp09SignTest start time：" + new Timestamp(System.currentTimeMillis()));

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");
        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example6/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据构建配置生成PDF
        JSONObject jsonData = (JSONObject)JSONObject.parse(jsonDataNode.toString());

        // 生成pdf路径
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example09_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example6/tpl.json"),jsonData,outputFile);

        String pageInfoJsonStr = pdfGenerator.pdfPageInfoRead(ResourceUtil.getResourceAsStream(outputFile));
        log.info(pageInfoJsonStr);
        Gson gson = new Gson();
        RayinMeta rayinMeta = gson.fromJson(pageInfoJsonStr, RayinMeta.class);
        List<MarkInfo> markInfoList = rayinMeta.getMarkInfos();
        List<SignatureProperty> spl = new ArrayList();
        for(MarkInfo m:markInfoList){
            if(m.getKeyword().equals("sign54321")){
                SignatureProperty s = new SignatureProperty();
                s.setPageNum(m.getPageNum());
                s.setX(m.getX());
                s.setY(m.getY());
                s.setWidth(m.getWidth());
                s.setHeight(m.getHeight());
                s.setSignatureImage("examples/example9/rayinsign.gif");
                spl.add(s);
            }
        }
        // 生成pdf路径
        // generate pdf path
//        outputFile =  new File(outputFileClass)
//                .getParentFile().getParent()
//                + "/tmp/"
//                + "example9_pdfbox_sign_"+System.currentTimeMillis() + ".pdf";
        pdfSign.multipleSign("123456","examples/example9/p12sign.p12","examples/example9/example6.pdf",
                outputFile,spl);
        log.info("exp09SignTest end time：" + new Timestamp(System.currentTimeMillis()));
    }


    /**
     * example10
     * 字体生成测试
     * single element generate test
     */
    @Test
    public void exp10FontsGenerateTest() throws Exception {
        log.info("exp10FontsGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example10_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        //数据参数可以为空
        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example10/element1.html"),null,outputFile);

        log.info("exp10FontsGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }
}
