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
import org.junit.Test;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Openhtmltopdf适配生成器测试类
 * Openhtmltopdf adapter generator test class
 */

@Slf4j
public class PdfBoxGeneratorOpenhtmltopdfExample {
    PdfGenerator pdfGenerator = new PdfBoxGenerator();
    Signature pdfSign = new PdfBoxSignature();
    public PdfBoxGeneratorOpenhtmltopdfExample() throws Exception {
        pdfGenerator.init();
    }

    /**
     * example1
     * 单个构件生成测试
     * single element generate test
     */
    @Test
    public void exp1ElementGenerateTest() throws Exception {
        log.info("exp1ElementGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String outputFile ="";
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        outputFile = (outputFile == null || outputFile.equals(""))? new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example1_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

        //数据参数可以为空
        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example1/element1.html"),null,outputFile);

        log.info("exp1ElementGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example2
     * 简单的模板生成测试
     * simple template generate test
     */
    @Test
    public void exp2SimpleTemplateGenerateTest() throws Exception {
        log.info("exp2SimpleTemplateGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String outputFile ="";
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        outputFile = (outputFile == null || outputFile.equals(""))? new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example2_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example2/tpl.json"),null,outputFile);

        log.info("exp2SimpleTemplateGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example3
     * 单个构件绑定数据生成测试
     * single element bind data generate test
     */
    @Test
    public void exp3ElementBindDataGenerateTest() throws Exception {
        log.info("exp3ElementBindDataGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example3/data.json");
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

        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example3/element1.html"),jsonData,outputFile);

        log.info("exp3ElementBindDataGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example4 绑定数据的模板生成测试
     * template bind data generate test
     */
    @Test
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

    /**
     * example5
     * 复杂构件绑定数据生成测试
     * single element bind data generate test
     */
    @Test
    public void exp5ComplexElementBindDataGenerateTest() throws Exception {
        log.info("exp5ComplexElementBindDataGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example5/data.json");
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

        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example5/element1.html"),jsonData,outputFile);

        log.info("exp5ComplexElementBindDataGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example6
     * 复杂模板配置生成测试
     * complex template generate test
     */
    @Test
    public void exp6ComplexTemplateGenerateTest() throws Exception {
        log.info("exp6ComplexTemplateGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example6/data.json");
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

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example6/tpl.json"),jsonData,outputFile);

        log.info("exp6ComplexTemplateGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example7
     * 特殊标签测试
     * special tag generate test
     */
    @Test
    public void exp7SpecialTagGenerateTest() throws Exception {
        log.info("exp7SpecialTagGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example7/data.json");
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

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example7/tpl.json"),jsonData,outputFile);

        log.info("exp7SpecialTagGenerateTest end time：" + new Timestamp(System.currentTimeMillis()));
    }


    /**
     * example8
     * 获取生成信息
     * get page info test
     */
    @Test
    public void exp8GetPageInfoTest() throws Exception {
        log.info("exp8GetPageInfoTest start time：" + new Timestamp(System.currentTimeMillis()));
        String outputFile ="";
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        outputFile = (outputFile == null || outputFile.equals(""))? new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example8_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example8/tpl.json"), null, outputFile);

        log.info(pdfGenerator.pdfPageInfoRead(ResourceUtil.getResourceAsStream(outputFile)));

        log.info("exp8GetPageInfoTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

    /**
     * example9
     * 签章
     * get page info test
     */
    @Test
    public void exp9SignTest() throws Exception {
        log.info("exp9SignTest start time：" + new Timestamp(System.currentTimeMillis()));
        String outputFile ="";
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");
        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example6/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据构建配置生成PDF
        JSONObject jsonData = (JSONObject)JSONObject.parse(jsonDataNode.toString());

        // 生成pdf路径
        outputFile = (outputFile == null || outputFile.equals(""))? new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example9_openhtmltopdf_"+System.currentTimeMillis() + ".pdf" : outputFile;

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
        log.info("exp9SignTest end time：" + new Timestamp(System.currentTimeMillis()));
    }

}
