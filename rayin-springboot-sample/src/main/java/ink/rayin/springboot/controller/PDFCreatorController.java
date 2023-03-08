package ink.rayin.springboot.controller;

import com.alibaba.fastjson2.JSONObject;
import ink.rayin.datarule.DataRule;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.base.Signature;
import ink.rayin.tools.utils.Charsets;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

@Slf4j
@RestController
@RequestMapping(value="/",produces = MediaType.APPLICATION_JSON_VALUE)
public class PDFCreatorController {
    @Resource
    PdfGenerator pdfGenerator;
    @Resource
    DataRule dataRule;
    @Resource
    Signature signature;

    /**
     * 项目模板生成至目录
     *
     * @param tplName
     * @param jsonData
     * @return
     */
    @PostMapping(value = "/pdf/create/tpl/{tplName}/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public void pdfCreateByTplToFile(@PathVariable("tplName") String tplName, @RequestBody JSONObject jsonData) throws Exception {
        // 生成pdf路径
        // generate pdf path
        //String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");
        String outputFile = "."
                + "/tmp/"
                + "example01_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";
        pdfGenerator.generatePdfFileByTplConfigFile("tpl/"+tplName + "/tpl.json" , jsonData, outputFile);
    }

    /**
     * 通过数据匹配模板生成PDF
     * 1. 先通过数据规则获取templateId
     * 2. 在通过数据获取数据转换脚本进行数据转换
     * 3. 再通过转换后的数据+templateId查找对应的模板生成pdf
     *
     * @param jsonData
     * @throws Exception
     */
    @PostMapping(value = "/pdf/create/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public void pdfCreateToFile(@RequestBody JSONObject jsonData) throws Exception {
        // templateId数据规则转换
        JSONObject tplIdData = (JSONObject)dataRule.executeGroovyFile(jsonData, null, "input", "other", "rules/tpl_id_convert.groovy");
        // 生成数据数据规则转换
        String scriptFileName = dataRule.getGroovyFileNameByData(tplIdData, "_", "public.orgId", "public.prdCode");
        JSONObject outputData = (JSONObject)dataRule.executeGroovyFile(tplIdData, null,"input", "other",
                "rules" + File.separator + scriptFileName);


        String templateId = tplIdData.getString("templateId");

        // 生成pdf路径
        // generate pdf path
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + templateId + "_"+System.currentTimeMillis() + ".pdf";
        pdfGenerator.generatePdfFileByTplConfigFile("tpl/" + templateId + "/tpl.json" , outputData, outputFile);
    }

    /**
     * 项目模板生成输出流
     *
     * @param tplName
     * @param jsonData
     * @return
     */
    @GetMapping(value = "/pdf/create/tpl/{tplName}/os")
    public void pdfCreateByTplToOS(@PathVariable("tplName") String tplName, @RequestBody(required = false) JSONObject jsonData, HttpServletResponse response) throws Exception {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        pdfGenerator.generatePdfStreamByTplConfigStr(ResourceUtil.getResourceAsString("tpl/"+tplName + "/tpl.json", Charsets.UTF_8) , jsonData, bao);
        response.setContentType("application/pdf;charset=utf-8");
        OutputStream stream = response.getOutputStream();
        stream.write(bao.toByteArray());
        stream.flush();
        stream.close();
    }


}