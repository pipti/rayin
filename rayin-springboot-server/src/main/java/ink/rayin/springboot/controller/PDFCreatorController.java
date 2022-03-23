package ink.rayin.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.tools.utils.Charsets;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@Slf4j
@RestController
@RequestMapping(value="/",produces = MediaType.APPLICATION_JSON_VALUE)
public class PDFCreatorController {
    @Autowired
    PdfGenerator pdfGenerator;


    /**
     * 项目模板生成接口
     *
     * @param tplName
     * @param jsonData
     * @return
     */
    @GetMapping(value = "/pdf/create/tpl/{tplName}/file", produces = MediaType.APPLICATION_JSON_VALUE)
    public void pdfCreateByTplToFile(@PathVariable("tplName") String tplName, @RequestBody JSONObject jsonData) throws Exception {
        pdfGenerator.generatePdfFileByTplConfigFile("tpl/"+tplName + "/tpl.json" , jsonData, "./tmp/a.pdf");
    }


    /**
     * 项目模板生成接口
     *
     * @param tplName
     * @param jsonData
     * @return
     */
    @PostMapping(value = "/pdf/create/tpl/{tplName}/os")
    public void pdfCreateByTplToOS(@PathVariable("tplName") String tplName, @RequestBody JSONObject jsonData, HttpServletResponse response) throws Exception {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        pdfGenerator.generatePdfStreamByTplConfigStr(ResourceUtil.getResourceAsString("tpl/"+tplName + "/tpl.json", Charsets.UTF_8) , jsonData, bao);
        response.setContentType("application/pdf;charset=utf-8");
        OutputStream stream = response.getOutputStream();
        stream.write(bao.toByteArray());
        stream.flush();
        stream.close();
    }


}