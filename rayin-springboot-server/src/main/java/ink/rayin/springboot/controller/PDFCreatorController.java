package ink.rayin.springboot.controller;

import com.alibaba.fastjson.JSONObject;
import ink.rayin.htmladapter.base.PdfGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value="/",produces = MediaType.APPLICATION_JSON_VALUE)
public class PDFCreatorController {
    @Autowired
    PdfGenerator pdfGenerator;


    /**
     * 项目模板生成接口
     *
     * @param tplNo
     * @param jsonData
     * @return
     */
    @GetMapping(value = "/pdf/createbytplno", produces = MediaType.APPLICATION_JSON_VALUE)
    public void pdfCreateByTplNo(@PathVariable("tplNo") String tplNo, @RequestBody JSONObject jsonData) throws Exception {
        pdfGenerator.generatePdfFileByTplConfigFile("tpl/"+tplNo , jsonData, "./tmp/a.pdf");
    }

}