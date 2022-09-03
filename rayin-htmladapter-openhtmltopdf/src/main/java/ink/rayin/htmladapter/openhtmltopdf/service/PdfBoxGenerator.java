/**
 * Copyright (c) 2022-2030, Janah Wang 王柱 (wangzhu@cityape.tech).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ink.rayin.htmladapter.openhtmltopdf.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.base.model.tplconfig.*;
import ink.rayin.htmladapter.base.thymeleaf.ThymeleafHandler;
import ink.rayin.htmladapter.base.utils.CSSParser;
import ink.rayin.htmladapter.base.utils.JsonSchemaValidator;
import ink.rayin.htmladapter.base.utils.RayinException;
import ink.rayin.htmladapter.openhtmltopdf.factory.OpenhttptopdfRenderBuilder;
import ink.rayin.htmladapter.openhtmltopdf.factory.OpenhttptopdfRendererObjectFactory;
import ink.rayin.htmladapter.openhtmltopdf.utils.PdfBoxPositionFindByKey;
import ink.rayin.tools.utils.*;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.encryption.*;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.*;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * pdf模板生成服务
 *
 * @author Jonah Wang
 */

public class PdfBoxGenerator implements PdfGenerator {
    private static Logger logger = LoggerFactory.getLogger(PdfBoxGenerator.class);

    private ThymeleafHandler thymeleafHandler = ThymeleafHandler.getInstance();

    private final String jsonSchema = "tpl_schema.json";
    private static JsonNode jsonSchemaNode;
    private final Base64.Encoder encoder = Base64.getEncoder();
    private final String os = System.getProperty("os.name");
    private final String tmpDir = System.getProperty("java.io.tmpdir");
    public PdfBoxGenerator() throws IOException {
            jsonSchemaNode = JsonSchemaValidator.getJsonNodeFromInputStream(ResourceUtil.getResourceAsStream(jsonSchema));

            if(jsonSchemaNode == null){
                new RayinException("json校验文件不存在！初始化失败");
            }
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#init()
     */
    @SneakyThrows
    @Override
    public void init(){
        OpenhttptopdfRendererObjectFactory.init();
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#init(String customizeFontPathDirectory)
     */
    @SneakyThrows
    @Override
    public void init(String customizeFontPathDirectory){
        OpenhttptopdfRendererObjectFactory.init(customizeFontPathDirectory);
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#init(int minIdle, int maxIdle, int maxTotal, String customizeFontPathDirectory)
     */
    @SneakyThrows
    @Override
    public void init(int minIdle, int maxIdle, int maxTotal, String customizeFontPathDirectory) {
        OpenhttptopdfRendererObjectFactory.init(minIdle, maxIdle, maxTotal, customizeFontPathDirectory);
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfFileByTplConfigFile
     */
    @SneakyThrows
    @Override
    public RayinMeta generatePdfFileByTplConfigFile(String templateLocation, JSONObject jsonData,
                                                    String outputFilePath) {
        return generatePdfFileByTplConfigStr(ResourceUtil.getResourceAsString(templateLocation, StandardCharsets.UTF_8), jsonData, outputFilePath);
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfFileByTplConfigStr
     */
    @SneakyThrows
    @Override
    public RayinMeta generatePdfFileByTplConfigStr(String tplConfigStr, JSONObject jsonData,
                                                    String outputFilePath){
        //通过path获取配置json
        JsonNode tplConfigJsonDataNode = JsonSchemaValidator.getJsonNodeFromString(tplConfigStr);

        //生成的文件
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String tplConfigJsonStr = tplConfigJsonDataNode.toString();
        RayinMeta fileInfo = generatePdfStreamByTplConfigStr(tplConfigJsonStr,jsonData,bos);

        //修改文件属性
        TemplateConfig pagesConfig = JSONObject.parseObject(tplConfigJsonStr, TemplateConfig.class);
        writePDFAttrs(bos,fileInfo,pagesConfig);

        File file = new File(outputFilePath);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(outputFilePath);
        try {
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();

            bos.close();
        }catch(Exception e){
            throw e;
        }finally {

            if(fos != null){
                try{
                    fos.close();
                }catch(IOException e){
                    logger.error("fos close error",e);
                }
            }
            if(bos != null){
                try{
                    bos.close();
                }catch(IOException e){
                    // log.error("fos close error",e);
                }
            }

        }

        return fileInfo;
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generateEncryptPdfStreamByConfigStr
     */
    @SneakyThrows
    @Override
    public RayinMeta generateEncryptPdfStreamByConfigStr(String tplConfigStr, JSONObject jsonData,
                                                         ByteArrayOutputStream os, String password) {

        //生成的文件
        RayinMeta fileInfo = generatePdfStreamByTplConfigStr(tplConfigStr,jsonData,os);

        //修改文件属性
        TemplateConfig pagesConfig = JSONObject.parseObject(tplConfigStr, TemplateConfig.class);
        if (StringUtil.isNotBlank(password)) {
            fileInfo.setSecretKey(password);
        }
        writePDFAttrs(os,fileInfo,pagesConfig);
        return fileInfo;

    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfStreamByTplConfigStr
     */
    @SneakyThrows
    @Override
    public RayinMeta generatePdfStreamByTplConfigStr(String tplConfigStr, JSONObject dataJson,
                                                     ByteArrayOutputStream os) {
        /** 校验配置开始 **/

        JsonNode tplConfigJsonDataNode = JsonSchemaValidator.getJsonNodeFromString(tplConfigStr);
        if(tplConfigJsonDataNode == null){
            new RayinException("json报文格式不正确！");
        }
        JsonSchemaValidator.validateJsonByFgeByJsonNode(tplConfigJsonDataNode, jsonSchemaNode);
        /** 校验配置结束 **/

        /** 生成文件开始 **/
        //依据构建配置生成PDF
        TemplateConfig templateConfig = JSONObject.parseObject(tplConfigStr, TemplateConfig.class);
        //遍历配置
        List<Element> pages = templateConfig.getElements();
        List<PageNumDisplayPos> templatePageNumDisplayPos = null;
        if(templateConfig.getPageNumDisplayPoss() != null && templateConfig.getPageNumDisplayPoss().size() > 0){
            templatePageNumDisplayPos = templateConfig.getPageNumDisplayPoss();
        }

        List<ByteArrayOutputStream> out = new ArrayList();
        //返回的相关实际配置信息
        List<Element> pageProperties = new ArrayList<>();


        for(Element el:pages){
            // 判断数据路径是否存在，如果为空则直接跳过
            try{
                if(StringUtil.isNotBlank(el.getElementAvaliableDataPath())){
                    JsonPath.read(dataJson,el.getElementAvaliableDataPath());
                }
            }catch(PathNotFoundException e){
                // 无法获取路径
                el.setPageNum(-1);
                continue;
            }
            //遍历构件并将生成的pdf字节流写入列表
            ByteArrayOutputStream tmp = generatePdfSteamByHtmlFileAndData(templateConfig,el,dataJson,pageProperties);
            if(tmp != null) {
                out.add(tmp);
            }
        }
        /** 生成文件结束 **/

        /** 写入页码开始 **/
        //写入页码开始 对生成后字节流进行遍历 读取配置写入页码
        int pageNum = 1;
        int pageNumTotal = 0;
        int i = 0;
        String footerStr = "";
        PageNumProperties pnp;
        for(Element pp:pageProperties) {
            if(pp.isPageNumIsCalculate() == true){
                pageNumTotal = pageNumTotal + pp.getPageCount();
            }
        }

        //遍历模板配置
        for(Element pp:pageProperties) {

            if (pp != null) {
                if (pp.isPageNumIsFirstPage() == true) {
                    pageNum = 1;
                }
                if (pp.isPageNumIsCalculate() == true) {

                    if(pp.isPageNumIsDisplay() == true){
                        //默认显示页码 添加到pdf中
                        footerStr = "第" + pageNum + "页，共" + pageNumTotal + "页";
                        pp.setLogicPageNum(pageNum);

                        PDDocument doc = PDDocument.load(out.get(i).toByteArray());

                        int j = 0;
                        //PdfContentByte content;
                        PDPage content;
                        int elPageNum = doc.getNumberOfPages();
                        while(true){
                            if(j >= elPageNum) {
                                break;
                            }
                            content = doc.getPage(j);
                            if(content != null){

                                float overContentHeight = content.getMediaBox().getHeight();
                                //获取自定义配置
                                if(pp.getPageNumDisplayPoss() != null && pp.getPageNumDisplayPoss().size() > 0) {
                                    //获取页码坐标
                                    for (PageNumDisplayPos pos : pp.getPageNumDisplayPoss()) {
                                        // 坐标页码设置
                                        String pageNumContent = footerStr;
                                        if (StringUtil.isNotBlank(pos.getContent())) {
                                            pageNumContent = pos.getContent().replace("${pageNum}", pageNum + "").replace("${pageNumTotal}", pageNumTotal + "");
                                        }

                                        PDPageContentStream contentStreamEl
                                                = new PDPageContentStream(doc, content, PDPageContentStream.AppendMode.APPEND, false);

                                        contentStreamEl.beginText();
                                        contentStreamEl.newLineAtOffset(pos.getX()==0f?content.getMediaBox().getWidth()/2 - 30:pos.getX(), pos.getY() != 0f?overContentHeight - pos.getY():20);

                                        contentStreamEl.setFont(PDType0Font.load(doc, OpenhttptopdfRendererObjectFactory.getFSSupplierCache().get(StringUtil.isNotBlank(pos.getFontFamily())?pos.getFontFamily():"FangSong").supply()), pos.getFontSize() != 0?pos.getFontSize():10);

                                        contentStreamEl.showText(pageNumContent);
                                        contentStreamEl.endText();
                                        contentStreamEl.close();


                                        // 关键字页码设置
                                        if(StringUtil.isNotBlank(pos.getMark())){

                                            List<float[]> fl = PdfBoxPositionFindByKey.findKeywordPagePostions(out.get(i).toByteArray(),pos.getMark(),j);

                                            if(fl.size() > 0){

                                                PDPageContentStream contentStream = new PDPageContentStream(doc, content,PDPageContentStream.AppendMode.APPEND, false);
                                                contentStream.beginText();
                                                for(float[] f:fl){
                                                contentStream.newLineAtOffset(f[0], f[1]);
                                                }
                                                contentStream.setFont(PDType0Font.load(doc, OpenhttptopdfRendererObjectFactory.getFSSupplierCache().get("FangSong").supply()), 10);
                                                contentStream.showText(footerStr);
                                                contentStream.endText();
                                                contentStream.close();
                                            }

                                        }


                                    }
                                }else {
                                    PDPageContentStream contentStream = new PDPageContentStream(doc, content,PDPageContentStream.AppendMode.APPEND, false);
                                    contentStream.beginText();
                                    contentStream.newLineAtOffset(content.getMediaBox().getWidth()/2 - 30, 20);
                                    contentStream.setFont(PDType0Font.load(doc, OpenhttptopdfRendererObjectFactory.getFSSupplierCache().get("FangSong").supply()), 10);
                                    contentStream.showText(footerStr);
                                    contentStream.endText();
                                    contentStream.close();
                                }

                            }else {
                                break;
                            }
                            pageNum++;
                            footerStr = "第" + pageNum + "页，共" + pageNumTotal + "页";
                            j++;
                        }
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        doc.save(out2);
                        doc.close();
                        out.set(i,out2);

                    }else{
                        pageNum++;
                    }

                }else{
                    //如果不计算，默认不显示
                }
            }
            i++;
        }
        /** 写入页码结束 **/

        //最终将生成的构件字节列表合并成文件
        List<Element> rp = new ArrayList<Element>(50);
        List<MarkInfo> signInfo = new ArrayList<MarkInfo>(50);
        RayinMeta efi = writeTargetFile(os, mergePDF(out).toByteArray());

        int calPageNum = 1;

        for(Element page:pages){
            Element pa = new Element();
            pa.setPageCount(page.getPageCount());
            pa.setPageNum(calPageNum);
            pa.setLogicPageNum(page.getLogicPageNum());
            pa.setElementType(page.getElementType());
            pa.setAddBlankPage(page.isAddBlankPage());
            pa.setPageNumIsCalculate(page.isPageNumIsCalculate());
            pa.setPageNumIsDisplay(page.isPageNumIsDisplay());
            pa.setPageNumIsFirstPage(page.isPageNumIsFirstPage());
            if(page.getMarkKeys() != null && page.getMarkKeys().size() > 0){
                page.getMarkKeys().forEach(s->{
                    try {
                        List<float[]> fl = PdfBoxPositionFindByKey.findKeywordPagesPostions(os.toByteArray(),s.getKeyword());
                        fl.forEach(f->{
                            MarkInfo mi = new MarkInfo();
                            mi.setKeyword(s.getKeyword());
                            mi.setPageNum((int)f[0]);
                            mi.setX(f[1]);
                            mi.setY(f[2]);
                            mi.setWidth(s.getWidth());
                            mi.setHeight(s.getHeight());
                            signInfo.add(mi);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            rp.add(pa);
            calPageNum = calPageNum + page.getPageCount();
        }

        efi.setPagesInfo(rp);
        efi.setMarkInfos(signInfo);

        return efi;
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfFileByHtmlAndData
     */
    @SneakyThrows
    @Override
    public boolean generatePdfFileByHtmlAndData(String htmlLocation, JSONObject jsonObject,
                                                String outputFilePath) {
        this.generatePdfFileByHtmlStr(ResourceUtil.getResourceAsString(htmlLocation, StandardCharsets.UTF_8), jsonObject, outputFilePath);

        return true;
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfSteamByHtmlStrAndData
     */
    @SneakyThrows
    @Override
    public ByteArrayOutputStream generatePdfSteamByHtmlStrAndData(String htmlStr, JSONObject jsonData) {
        String htmlContent = htmlStrDataFilling(htmlStr, jsonData);
        return this.generatePdfStreamByHtmlStr(htmlContent);
    }



    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfSteamByHtmlFileAndData
     */
    @SneakyThrows
    private ByteArrayOutputStream generatePdfSteamByHtmlFileAndData(TemplateConfig pagesConfig, Element config, JSONObject data, List<Element> pp) {

        String htmlContent = "";

        if(StringUtil.isNotBlank(config.getContent())) {
            htmlContent = htmlStrDataFilling(config.getContent(), data);
        }

        if(StringUtil.isNotBlank(config.getElementPath())){
            htmlContent = htmlFileDataFilling(config.getElementPath(), data);
        }

        //logger.info(htmlContent);
        Set<MarkInfo> signature = new HashSet<MarkInfo>();
        ByteArrayOutputStream bo = this.generatePdfStreamByHtmlStr(htmlContent, signature);
        config.setMarkKeys(signature);
        if(bo == null){
            return null;
        }
        //PdfReader reader;
        PDDocument pdfDoc = PDDocument.load(bo.toByteArray());

        int pageNum = 0;
        // boolean calPageNum  = false;
        if(bo != null) {

            //reader = new PdfReader(bo.toByteArray());
            pageNum = pdfDoc.getNumberOfPages();

            // 增加空白页
            if(config.isAddBlankPage() == true && pageNum%2 == 1){
                ByteArrayOutputStream blankOS ;
                //如果配置空白页样式
                if(StringUtil.isNotBlank(config.getBlankElementPath())){
                    blankOS = generatePdfSteamByHtmlFileAndData(config.getBlankElementPath(),data);
                }else{
                    if(StringUtil.isNotBlank(pagesConfig.getBlankElementPath())){
                        blankOS = generatePdfSteamByHtmlFileAndData(pagesConfig.getBlankElementPath(),data);
                    }else {
                        //如果没有配置获取默认样式
                        blankOS = generatePdfSteamByHtmlStrAndData(ResourceUtil.getResourceAsString("blank.html",StandardCharsets.UTF_8), data);
                    }
                }
                List<ByteArrayOutputStream> clByte = new ArrayList();
                ByteArrayOutputStream blankByte = blankOS;
                clByte.add(bo);
                clByte.add(blankByte);
                bo = this.mergePDF(clByte);
                pageNum++;
            }
            //记录当前构件的页数
            config.setPageCount(pageNum);
        }
        pp.add(config);

        pdfDoc.close();
        return bo;
    }


    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#htmlFileDataFilling
     */
    @SneakyThrows
    @Override
    public String htmlFileDataFilling(String htmlLocation, JSONObject data) {
        String htmlContent = "";

        htmlContent = thymeleafHandler.templateEngineProcessByPath(htmlLocation, data);

        return htmlContent;
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#htmlStrDataFilling
     */
    @SneakyThrows
    @Override
    public String htmlStrDataFilling(String htmlStr, JSONObject data) {
        String htmlContent = "";
        if(StringUtil.isBlank(htmlStr)) {
            return "";
        };

        htmlContent = thymeleafHandler.templateEngineProcessByString(htmlStr, data);

        return htmlContent;
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfFileByHtmlStr(String htmlStr, String outputFile)
     */
    @SneakyThrows
    @Override
    public void generatePdfFileByHtmlStr(String htmlStr, String outputFile){

        File f = new File(outputFile);
        if (f != null && !f.getParentFile().exists()) {
            f.getParentFile().mkdir();
        }
        FileUtils.writeByteArrayToFile(new File(outputFile), generatePdfStreamByHtmlStr(htmlStr).toByteArray());
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfFileByHtmlStr(String htmlStr, JSONObject data, String outputFile)
     */
    @SneakyThrows
    @Override
    public void generatePdfFileByHtmlStr(String htmlStr, JSONObject data, String outputFile){
        generatePdfFileByHtmlStr(htmlStrDataFilling(htmlStr,data),outputFile);
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfStreamByHtmlStr
     */
    @SneakyThrows
    @Override
    public ByteArrayOutputStream generatePdfStreamByHtmlStr(String htmlContent){
        return generatePdfStreamByHtmlStr(htmlContent, null);
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfStreamByHtmlStr
     */
    private ByteArrayOutputStream generatePdfStreamByHtmlStr(String htmlContent, Set<MarkInfo> markKeys)
            throws Exception {
        //解析是否存在embed标签，并对其进行解析
        List<ByteArrayOutputStream> apendFiles =  new ArrayList<>();

        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(htmlContent);

        Elements pdfHiddenEls = htmlDoc.getElementsByAttributeValueContaining("type", "pdf/hidden");
        for (org.jsoup.nodes.Element ele : pdfHiddenEls) {
            ele.remove();
        }

        Elements imgLinks = htmlDoc.getElementsByTag("img");

        for(org.jsoup.nodes.Element link : imgLinks){
            String src = link.attr("src");
            if(StringUtil.isNotBlank(src)){
                if (src.startsWith("http://") || src.startsWith("https://") || src.startsWith("file:") || src.startsWith("data:image/")) {
                    if(src.startsWith("file:")){
                        src = src.replace("\\", "/");
                    }
                }else if (src.startsWith("/") || src.startsWith("\\")) {
                    src = "file:" + "//" + src;
                    src = src.replace("\\" , "/");
                    logger.debug("image url convert:" + src);
                }else{
                    if(os != null && os.toLowerCase().startsWith("windows")){
                        src = "file:" + "///" + ResourceUtil.getResourceAbsolutePathByClassPath(src);
                    }else{
                        src = "file:" + "//" + ResourceUtil.getResourceAbsolutePathByClassPath(src);
                    }
                    src = src.replace("\\" , "/");
                    logger.debug("image url convert:" + src);
                }
                link.attr("src", src);
            }
        }

        Elements bgEls = htmlDoc.getElementsByAttributeValueContaining("style", "background");
        String pattern = "[\\s\\S]*url\\((?<url>.*?)\\)[\\s\\S]*";
        Pattern r = Pattern.compile(pattern);
        for (org.jsoup.nodes.Element ele : bgEls) {
            String bgCssStr = CSSParser.getSingleStylePropertyValue(ele.attr("style"), "background");
            logger.debug(CSSParser.getSingleStylePropertyValue(ele.attr("style"), "background"));
            if(bgCssStr.indexOf("url") >= 0){
                Matcher matcher = r.matcher(bgCssStr);

                while (matcher.find()){
                    String url = matcher.group("url");
                    String newUrl = url;
                    if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file:") || url.startsWith("data:image/")) {
                        if(url.startsWith("file:")){
                            url = url.replace("\\", "/");
                        }
                    }else if (url.startsWith("/") || url.startsWith("\\")) {
                        newUrl = "file:" + "//" + url;
                        newUrl = newUrl.replace("\\" , "/");
                        logger.debug("image url convert:\'" + newUrl + "'");
                    }else{
                        if (os != null && os.toLowerCase().startsWith("windows")){
                            newUrl = "file:" + "///" + ResourceUtil.getResourceAbsolutePathByClassPath(url);
                        }else{
                            newUrl = "file:" + "//" + ResourceUtil.getResourceAbsolutePathByClassPath(url);
                        }
                        newUrl = newUrl.replace("\\" , "/");
                        logger.debug("image url convert:\'" + newUrl + "'");
                    }
                    bgCssStr = bgCssStr.replace(url, newUrl);
                }

                CSSStyleDeclaration cssStyleDeclaration = CSSParser.addSingleStyleProperty(ele.attr("style"), "background", bgCssStr, null);
                bgEls.attr("style", cssStyleDeclaration.getCssText());
            }
        }
        Elements bgImgEls = htmlDoc.getElementsByAttributeValueContaining("style", "background-image");
        for (org.jsoup.nodes.Element ele : bgImgEls) {
            String bgCssStr = CSSParser.getSingleStylePropertyValue(ele.attr("style"), "background-image");
            logger.debug(CSSParser.getSingleStylePropertyValue(ele.attr("style"), "background-image"));
            if(bgCssStr.indexOf("url") >= 0){
                Matcher matcher = r.matcher(bgCssStr);

                while (matcher.find()){
                    String url = matcher.group("url");
                    String newUrl = url;

                    if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file:") || url.startsWith("data:image/")) {
                        if(url.startsWith("file:")){
                            url = url.replace("\\", "/");
                        }
                    }else if (url.startsWith("/") || url.startsWith("\\")) {
                        newUrl = "file:" + "//" + url;
                        newUrl = newUrl.replace("\\" , "/");
                        logger.debug("image url convert:\'" + newUrl + "'");
                    }else{
                        if (os != null && os.toLowerCase().startsWith("windows")){
                            newUrl = "file:" + "///" + ResourceUtil.getResourceAbsolutePathByClassPath(url);
                        }else{
                            newUrl = "file:" + "//" + ResourceUtil.getResourceAbsolutePathByClassPath(url);
                        }
                        newUrl = newUrl.replace("\\" , "/");
                        logger.debug("image url convert:\'" + newUrl + "'");
                    }
                    bgCssStr = bgCssStr.replace(url, newUrl);
                }

                CSSStyleDeclaration cssStyleDeclaration = CSSParser.addSingleStyleProperty(ele.attr("style"), "background-image", bgCssStr, null);
                bgEls.attr("style", cssStyleDeclaration.getCssText());
            }
        }

//        Elements styleTagEls = htmlDoc.getElementsByTag("style");
//        String cssNew = "";
//        for (org.jsoup.nodes.Element ele : styleTagEls) {
//            InputSource source = new InputSource(new StringReader(ele.html()));
//            source.setEncoding("UTF-8");
//            final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
//            CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
//            CSSRuleList rules = sheet.getCssRules();
//            String selectorText_= null;
//            CSSStyleDeclaration ss = null;
//            for (int i = 0; i < rules.getLength(); i++) {
//                final CSSRule rule = rules.item(i);
//                //获取选择器名称
//                if(rule instanceof CSSPageRule) {
//                    selectorText_ = ((CSSPageRule) rule).getSelectorText();
//                    ss =  ((CSSPageRule)rule).getStyle();
//                }
//                if(rule instanceof CSSStyleRule) {
//                    selectorText_ = ((CSSStyleRule) rule).getSelectorText();
//                    ss =  ((CSSStyleRule)rule).getStyle();
//                }
//
//                String background = ss.getPropertyValue("background");
//                String backgroundImage = ss.getPropertyValue("background-image");
//                if(StringUtil.isNotBlank(background) && background.indexOf("url") >= 0){
//                    Matcher matcher = r.matcher(background);
//
//                    while (matcher.find()){
//                        String url = matcher.group("url");
//                        String newUrl = url;
//
//                        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file:") || url.startsWith("data:image/")) {
//                            if(url.startsWith("file:")){
//                                url = url.replace("\\", "/");
//                            }
//                        }else if (url.startsWith("/") || url.startsWith("\\")) {
//                            newUrl = "file:" + "//" + url;
//                            newUrl = newUrl.replace("\\" , "/");
//                            logger.debug("image url convert:\'" + newUrl + "'");
//                        }else{
//                            if (os != null && os.toLowerCase().startsWith("windows")){
//                                newUrl = "file:" + "///" + ResourceUtil.getResourceAbsolutePathByClassPath(url);
//                            }else{
//                                newUrl = "file:" + "//" + ResourceUtil.getResourceAbsolutePathByClassPath(url);
//                            }
//                            newUrl = newUrl.replace("\\" , "/");
//                            logger.debug("image url convert:\'" + newUrl + "'");
//                        }
//                        background = background.replace(url, newUrl);
//                    }
//                    //ss = CSSParser.addSingleStyleProperty(ss, "background" , background, null);
//                    //CSSStyleDeclaration cd =  ((CSSStyleRule)rule).getStyle();
//                    ss.setProperty("background", background, null);
//                    //cssNew = CSSParser.addRuleProperty(ele.data(), selectorText_, "background", background, null).toString();
//                }
//
//                if(StringUtil.isNotBlank(backgroundImage) && backgroundImage.indexOf("url") >= 0){
//                    Matcher matcher = r.matcher(backgroundImage);
//
//                    while (matcher.find()){
//                        String url = matcher.group("url");
//                        String newUrl = url;
//
//                        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file:") || url.startsWith("data:image/")) {
//                            if(url.startsWith("file:")){
//                                url = url.replace("\\", "/");
//                            }
//                        }else if (url.startsWith("/") || url.startsWith("\\")) {
//                            newUrl = "file:" + "//" + url;
//                            newUrl = newUrl.replace("\\" , "/");
//                            logger.debug("image url convert:\'" + newUrl + "'");
//                        }else{
//                            if (os != null && os.toLowerCase().startsWith("windows")){
//                                newUrl = "file:" + "///" + ResourceUtil.getResourceAbsolutePathByClassPath(url);
//                            }else{
//                                newUrl = "file:" + "//" + ResourceUtil.getResourceAbsolutePathByClassPath(url);
//                            }
//                            newUrl = newUrl.replace("\\" , "/");
//                            logger.debug("image url convert:\'" + newUrl + "'");
//                        }
//                        backgroundImage = backgroundImage.replace(url, newUrl);
//                    }
//                    //ss = CSSParser.addSingleStyleProperty(ss, "background" , background, null);
//                    //CSSStyleDeclaration cd =  ((CSSStyleRule)rule).getStyle();
//                    ss.setProperty("background-image", backgroundImage, null);
//                    //cssNew = CSSParser.addRuleProperty(ele.data(), selectorText_, "background", background, null).toString();
//                }
////                if("".equals(propertyValue) || propertyValue == null){
////                    return false;
////                }
//            }
//            ele.html(sheet.toString());
//        }





//        if(rules.getLength() == 0 ){
//            return false;
//        }
//        for (int i = 0; i < rules.getLength(); i++) {
//            final CSSRule rule = rules.item(i);
//            //获取选择器名称
//            String selectorText_ = ((CSSStyleRule) rule).getSelectorText();
//            if(selectorStr.equals(selectorText_)){
//                CSSStyleDeclaration ss =  ((CSSStyleRule)rule).getStyle();
//                String propertyValue = ss.getPropertyValue(property);
//                if("".equals(propertyValue) || propertyValue == null){
//                    return false;
//                }
//                return true;
//            }
//        }

//        for (org.jsoup.nodes.Element ele : bgImgEls) {
//            logger.debug(CSSParser.getSingleStylePropertyValue(ele.attr("style"), "background-image"));
//        }


//        Elements printHideStyleEls = htmlDoc.getElementsByAttributeValueContaining("style","-fs-pdf-hidden");
//        for (org.jsoup.nodes.Element ele : printHideStyleEls) {
//            if(CSSParser.checkSingleStylePropertyAndValue(ele.attr("style"), "-fs-pdf-hidden", "true")){
//                CSSStyleDeclaration csd = CSSParser.deleteSingleStyleProperty(ele.attr("style"), "background-image");
//                csd.removeProperty("background");
//                ele.attr("style", csd.toString());
//            }
//            //ele.attr("style",ele.attr("style").replaceAll("(background-image([\\s\\S]*):([\\s\\S]*)url([\\s\\S]*)\\(([\\s\\S]*)\\)|background([\\s\\S]*):([\\s\\S]*)url([\\s\\S]*)\\(([\\s\\S]*)\\))",""));
//        }
//
//        Elements headStyle = htmlDoc.getElementsByTag("style");
//        for(org.jsoup.nodes.Element ele : headStyle){
//            if(CSSParser.checkCssPropertyAndValue(ele.html(), "body", "-fs-pdf-hidden", "true")){
//                CSSStyleSheet cs1 = CSSParser.deleteRuleProperty(ele.html(), "body", "background-image");
//                CSSStyleSheet cs2 = CSSParser.deleteRuleProperty(cs1, "body", "background");
//                ele.html(cs2.toString());
//            }
//        }
        Elements obPdElements = htmlDoc.getElementsByAttributeValueContaining("type", "file/pdf");
        if(obPdElements.size() > 0){
            org.jsoup.nodes.Document htmlDocNew = Jsoup.parse(htmlContent);
            Elements obPdElements2 = htmlDocNew.getElementsByAttributeValueContaining("type", "file/pdf");
            obPdElements2.forEach(e->{e.remove();});
            List<DataNode> body = htmlDocNew.getElementsByTag("body").dataNodes();
            if(body.size() > 0){
                apendFiles.add(this.generatePdfStreamByHtmlStr(htmlDocNew.html()));
            }
        }
        Elements objectLinks = htmlDoc.getElementsByTag("object");
        for (org.jsoup.nodes.Element link : objectLinks) {
            String inner = link.text();
            String type = link.attr("type");
            String value = link.attr("value");
            String pages = link.attr("page");

            String style = link.attr("style");
            String[] styles = style.split(";");
            
            if(StringUtil.isNotBlank(type) && StringUtil.isNotBlank(value)){
                float width = 0;
                float height = 0;
                switch(type){
                    case "file/pdf":
                        if(StringUtil.isBlank(value)) {
                            continue;
                        }
                        org.jsoup.nodes.Element p = link.parent();
                        link.remove();
                        org.jsoup.nodes.Element pp = p.parent();
                        org.jsoup.nodes.Element box = p;
                        if(StringUtil.isBlank(p.html().replaceAll("[\n &nbsp;]",""))){
                            p.remove();
                            box = pp;
                        }

                        ByteArrayOutputStream pdfOs = null;
//                        try{
                            pdfOs = ResourceUtil.getResourceAsByte(value);
//                        }catch (FileNotFoundException fe){
//                            throw new RayinException("无法找到文件：" + fe.getMessage());
//                        }


                        PDDocument doc = PDDocument.load(pdfOs.toByteArray());

                        int elPageNum = doc.getNumberOfPages();

                        if (value.startsWith("http://") || value.startsWith("https://") || value.startsWith("file:")) {
                            if(value.startsWith("file:")){
                                value = value.replace("\\", "/");
                            }
                        }else if (value.startsWith("/") || value.startsWith("\\")) {
                            value = "file:" + "//" + value;
                            value = value.replace("\\" , "/");
                            logger.debug("pdf url convert:\'" + value + "'");
                        }else{
                            if (os != null && os.toLowerCase().startsWith("windows")){
                                value = "file:" + "///" + ResourceUtil.getResourceAbsolutePathByClassPath(value);
                            }else{
                                value = "file:" + "//" + ResourceUtil.getResourceAbsolutePathByClassPath(value);
                            }
                            value = value.replace("\\" , "/");
                            logger.debug("pdf url convert:\'" + value + "'");
                        }

                        if(StringUtil.isNotBlank(pages)){
                            String[] pageB = pages.split(",");
                            PDDocument extractDoc = new PDDocument();
                            for(String k : pageB){
                                //box.append("<img width=\"100%\" src=\""+ value +"\" page=\""+ k + "\"/>\n");
                                extractDoc.addPage(doc.getPage(Integer.valueOf(k)));
                            }
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            extractDoc.save(os);
                            apendFiles.add(os);
                        }else{
                            apendFiles.add(ResourceUtil.getResourceAsByte(value.substring(7)));

//                            for(int i = 1; i <= elPageNum; i++){
//                                //content = doc.getPage(i - 1);
//                                //overContentWidth = content.getMediaBox().getWidth();
//                                box.append("<img width=\"100%\" src=\""+ value +"\" page=\""+ i + "\"/>\n");
//                            }
                        }

                        doc.close();
                        pdfOs.close();
                        break;
                    case "mark":
                        if(markKeys != null){

                            //String border = "";
                            for(String st:styles){
                                if(st.indexOf("width:") >= 0){
                                    width = DisplayMeasureConvert.webMeasureToPDFPoint(st.toLowerCase().replace("width:",""));
                                    continue;
                                }
                                if(st.indexOf("height:") >= 0){
                                    height = DisplayMeasureConvert.webMeasureToPDFPoint(st.toLowerCase().replace("height:",""));
                                    continue;
                                }
                            }

                            MarkInfo markInfo = new MarkInfo();
                            markInfo.setKeyword(value);
                            markInfo.setWidth(width);
                            markInfo.setHeight(height);
                            markKeys.add(markInfo);
                        }
                        link.attr("style", StringUtil.isBlank(link.attr("style"))?"": "margin:"+ width/2 + "pt;color:white;display:inline;font-size:0.1px;border:0;" + (link.attr("style")+";"));
                        link.append(value);

                        break;

                    case "fontwatermark":
                        CSSStyleDeclaration cssStyleDeclaration = CSSParser.addSingleStyleProperty(link.attr("style"), "position","absolute", null);
                        cssStyleDeclaration = CSSParser.addSingleStyleProperty(cssStyleDeclaration, "left","600px",null);
                        cssStyleDeclaration = CSSParser.addSingleStyleProperty(cssStyleDeclaration, "top","300px",null);
                        if(!CSSParser.checkSingleStyleProperty(link.attr("style"), "transform")){
                            link.attr("style", cssStyleDeclaration.getCssText() + ";transform:rotate(40deg)");
                        }else{
                            link.attr("style", cssStyleDeclaration.getCssText());
                        }

                }

            }

        }
        if(apendFiles.size() > 0){
            return mergePDF(apendFiles);
        }

        OpenhttptopdfRenderBuilder openhttptopdfRenderBuilder = null;
        PdfRendererBuilder pdfRendererBuilder = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            openhttptopdfRenderBuilder = OpenhttptopdfRendererObjectFactory.getPdfRendererBuilderInstance();

            pdfRendererBuilder = openhttptopdfRenderBuilder.getPdfRendererBuilder();

            try {
                //避免使用Jsoup转换字符串进行直接转换w3c,document,因为不是严格的xml格式，转换存在问题
                //PDFCreationListener pdfCreationListener = new XHtmlMetaToPdfInfoAdapter(w3cDoc);
                //使用Jsoup也可以规范相关html
                W3CDom w3cDom = new W3CDom();
                //这里的doc对象指的是jsoup里的Document对象
                org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(htmlDoc);

                pdfRendererBuilder.withW3cDocument(w3cDoc,"/");

                pdfRendererBuilder.toStream(out);

                pdfRendererBuilder.run();

            } catch (Exception e) {
                logger.error(htmlDoc.toString());
                throw e;
            }

        } catch (Exception e) {
            throw e;
        } finally {
            OpenhttptopdfRendererObjectFactory.returnPdfBoxRenderer(openhttptopdfRenderBuilder);
        }

        PDDocument pdDoc = PDDocument.load(out.toByteArray());
        PDDocumentInformation pdInfo = pdDoc.getDocumentInformation();
        pdInfo.setProducer(
                UnicodeUtil.decode("\\u0040\\u004d\\u0041\\u0044\\u0045\\u0020\\u0042\\u0059\\u0020\\u0052\\u0041\\u0059\\u0049\\u004e"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pdDoc.save(baos);
        pdDoc.close();

        return baos;
    }


    /**
     * 把dom文件转换为xml字符串
     */
    private static String toStringFromDoc(org.w3c.dom.Document document) {
        String result = null;
        if (document != null) {
            StringWriter strWtr = new StringWriter();
            StreamResult strResult = new StreamResult(strWtr);
            TransformerFactory tfac = TransformerFactory.newInstance();
            try {
                javax.xml.transform.Transformer t = tfac.newTransformer();
                t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                t.setOutputProperty(OutputKeys.INDENT, "yes");
                // xml, html,
                t.setOutputProperty(OutputKeys.METHOD, "xml");
                // text
                t.setOutputProperty(
                        "{http://xml.apache.org/xslt}indent-amount", "4");
                t.transform(new DOMSource(document.getDocumentElement()),
                        strResult);
            } catch (Exception e) {
                System.err.println("XML.toString(Document): " + e);
            }
            result = strResult.getWriter().toString();
            try {
                strWtr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 合并PDF
     * @param pdfs 要合并的输入流
     * @return 返回合并后的流 存放在内存中
     */
    private ByteArrayOutputStream mergePDF(List<ByteArrayOutputStream> pdfs) throws Exception {

        // 如果要合并的列表为空则返回NULL，如果列表中只有一个对象，则返回该对象
        // 否则合并列表中的对象
        if (pdfs == null || pdfs.isEmpty()) {
            return null;
        } else if (pdfs.size() == 1) {
            return pdfs.get(0);
        }

        // 合并后的文档保存在内存中
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<InputStream> pdfIs = new ArrayList<InputStream>();
        for (ByteArrayOutputStream os : pdfs) {
            pdfIs.add(new ByteArrayInputStream(os.toByteArray()));
        }
        PDFMergerUtility mergePdf = new PDFMergerUtility();
        mergePdf.addSources(pdfIs);
        mergePdf.setDestinationStream(out);
        mergePdf.mergeDocuments(MemoryUsageSetting.setupMixed(1000000));

        return out;
    }

    /**
     * 将字节流生成文件
     * @param targetFile 目标文件
     * @param outByte 元文件字节
     * @return 元数据信息
     * @throws Exception exception
     */
    public RayinMeta writeTargetFileByByte(String targetFile, byte[] outByte)
            throws Exception {
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();


        RayinMeta fileInfo = writeTargetFile(out1,outByte);

        File file = new File(targetFile);
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file.createNewFile();

        try (FileOutputStream fos = new FileOutputStream(targetFile)){
            fos.write(out1.toByteArray());
            fos.flush();
            fos.close();
        }catch(Exception e){
            throw e;
        }
        //文件路径
        fileInfo.setFilePath(targetFile);

        //文件构建相关信息
        return fileInfo;
    }

    /**
     * 将字节流生成文件
     * @param os 输出流
     * @param outByte 待生成文件byte
     * @return 元数据信息
     */
    private RayinMeta writeTargetFile(ByteArrayOutputStream os, byte[] outByte)
            throws Exception {
        PDDocument doc = PDDocument.load(outByte);
        doc.save(os);
        doc.close();

        RayinMeta rayinMetadata = new RayinMeta();
        //文件总页数
        int pages=doc.getNumberOfPages();

        rayinMetadata.setFileTotalPageCount(pages);

        return rayinMetadata;
    }

    /**
     * 写入PDF文件属性
     * @param os 输出流
     * @param epfileInfo 元数据
     * @param pagesConfig 页面配置属性
     * @return
     */
    private void writePDFAttrs(ByteArrayOutputStream os, RayinMeta epfileInfo, TemplateConfig pagesConfig) throws Exception {

        ByteArrayOutputStream osCopy = new ByteArrayOutputStream();
        IOUtils.write(os.toByteArray(),osCopy);
        PDDocument doc = PDDocument.load(osCopy.toByteArray());

        PDDocumentInformation info = doc.getDocumentInformation();
        if(!pagesConfig.isEditable() || StringUtil.isNotBlank(pagesConfig.getPassword())) {
            AccessPermission permissions = new AccessPermission();
            permissions.setCanModify(pagesConfig.isEditable());
            permissions.setCanExtractContent(pagesConfig.isEditable());

            StandardProtectionPolicy standardProtectionPolicy = new StandardProtectionPolicy(pagesConfig.getPassword(), pagesConfig.getPassword(), permissions);
            SecurityHandler securityHandler = new StandardSecurityHandler(standardProtectionPolicy);
            securityHandler.prepareDocumentForEncryption(doc);
            PDEncryption encryption = new PDEncryption();
            encryption.setSecurityHandler(securityHandler);
            doc.setEncryptionDictionary(encryption);
        }
        info.setAuthor(pagesConfig.getAuthor());
        info.setTitle(pagesConfig.getTitle());
        info.setSubject(pagesConfig.getSubject());
        info.setKeywords(pagesConfig.getKeywords());
        info.setProducer(
                UnicodeUtil.decode("\\u0040\\u004d\\u0041\\u0044\\u0045\\u0020\\u0042\\u0059\\u0020\\u0052\\u0041\\u0059\\u0049\\u004e"));
        info.setCreator(UnicodeUtil.decode("\\u0040\\u004d\\u0041\\u0044\\u0045\\u0020\\u0042\\u0059\\u0020\\u0052\\u0041\\u0059\\u0049\\u004e"));
        info.setCreationDate(Calendar.getInstance());
        //写入文件相关配置信息包括页码，单模板类型以及页码起始页
        final Base64.Encoder encoder = Base64.getEncoder();
        logger.info(JSONObject.toJSONString(epfileInfo));
        info.setCustomMetadataValue("PagesInfo",encoder.encodeToString(JSONObject.toJSONString(epfileInfo).getBytes()));
        os.reset();
        doc.save(os);
        doc.close();
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfSteamByHtmlFileAndData
     */
    @SneakyThrows
    @Override
    public ByteArrayOutputStream generatePdfSteamByHtmlFileAndData(String templatePath, JSONObject jsonData){

        String htmlContent = htmlFileDataFilling(templatePath,
                jsonData);
        return this.generatePdfStreamByHtmlStr(htmlContent);
    }


    private String HTMLTrim(String html){
        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(html);
        List<Node> ln = htmlDoc.getElementsByTag("body").get(0).childNodes();

        for(Node n:ln){
            NodeTrim(n);
        }
        StringBuffer sb = new StringBuffer(html.length() + 1);
        for(Node n : ln){
            sb.append(n);
        }
        return sb.toString();
    }

    private void NodeTrim(Node n){
        if (n.childNodeSize() == 0) {
            if (n instanceof org.jsoup.nodes.Element || n instanceof Comment) {
                return ;
            } else {
                ((TextNode) n).text(n.toString().replaceAll("(\r|\n|\\s+)", ""));
            }
        }else{
            for (Node nc:n.childNodes()){
                NodeTrim(nc);
            }
        }
    }


    /**
     * substring 忽略HTML标签
     * @param html
     * @param beginIndex
     * @param endIndex
     * @return
     */
    private String SubStringIgnoreHtml(String html,int beginIndex ,int endIndex){
        //html  = html.replaceAll("(\r|\n)", "");
        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(html);

        List<Node> ln = htmlDoc.getElementsByTag("body").get(0).childNodes();
        List<Node> bn = new ArrayList<Node>();

        int offset = 0;
        for(Node n:ln){
            if(offset > endIndex || offset == -1) {
                break;
            }else {
                offset = calText(n, offset, beginIndex, endIndex);
                if(offset > beginIndex && offset != -1 && offset < endIndex) {
                    bn.add(n);
                }
                if(offset == endIndex){
                    bn.add(n);
                    break;
                }
            }
        }
        StringBuffer sb = new StringBuffer(endIndex - beginIndex + 1);
        for(Node n : bn){
            sb.append(n);
        }
        // System.out.println(sb.toString());

        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        String html = "<br><br>html文本测试带标签的值，<b>佛挡杀佛</b><br>fdsf,f对方身份的<br><br><br>433ffdsfds";

        PdfBoxGenerator pbts = new PdfBoxGenerator();
        int offset = 0;
        pbts.SubStringIgnoreHtml(html,0,0 + 5);
        pbts.SubStringIgnoreHtml(html,0 + 5,0 + 10);
        pbts.SubStringIgnoreHtml(html,0 + 10,0 + 50);
    }


    private int calText(Node n,int offset,int beginIndex,int endIndex){
        if(n.childNodeSize() == 0) {
            if (n instanceof org.jsoup.nodes.Element || n instanceof Comment) {
                return offset;
            } else {
                String tmp = n.toString().replaceAll("(\r|\n|\\s+)", "");
                int nodelen = tmp.length();
                if (offset + nodelen < beginIndex) {
                    offset = offset + nodelen;
                    return offset;
                }
                if(offset + nodelen == beginIndex){
                    offset = offset + nodelen;
                    return offset;
                }
                if ((offset + nodelen > beginIndex)  && (offset + nodelen < endIndex)) {
                    if (offset < beginIndex && n instanceof TextNode) {
                        ((TextNode) n).text(tmp.substring(beginIndex - offset, nodelen));
                    } else {
                        if(n instanceof TextNode){
                            ((TextNode) n).text(tmp);
                        }

                    }
                    offset = offset + nodelen;
                    return offset;
                }

                if ((offset + nodelen) >= endIndex && offset < endIndex) {
                    if(offset == endIndex){
                        return -1;
                    }else{
                        //System.out.println("text:" + tmp.substring(0, endIndex - offset));
                        if(n instanceof TextNode){
                            ((TextNode) n).text(tmp.substring(0, endIndex - offset));
                        }
                        offset = endIndex;
                        //offset = offset + nodelen;

                    }
                    return offset;
                }
            }
        }else{
            List<Node> ln = n.childNodes();
            for (int i = 0; i < ln.size(); i++) {

                if(offset != endIndex && offset != -1){
                    offset = calText(ln.get(i),offset,beginIndex,endIndex);
                }

                if(offset <= beginIndex && offset != -1){
                    ln.get(i).remove();
                    i--;
                    continue;
                }

                if((offset == endIndex || offset == -1) && (i + 1< ln.size())){
                    ln.get(i + 1).remove();
                    i--;
                    continue;
                }
            }

            return offset;
        }
        return offset;
    }

    Pattern pattern = Pattern.compile("\\d+");

    /**
     * 截取数字
     * @param content
     * @return 被截取后的字符串
     */
    public String getNumbers(String content) {
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#getFontNames
     */
    @Override
    public LinkedHashSet<String> getFontNames(){
        return OpenhttptopdfRendererObjectFactory.getFontNames();
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfFilesByTplAndExcel
     */
    @SneakyThrows
    @Override
    public void generatePdfFilesByTplAndExcel(String tplConfigStr, InputStream excelIs, String outputDirPath, String fileNamePrefix) {
        JSONArray ja = JSONObject.parseObject(EasyExcelUtils.readWithoutHead(excelIs), JSONArray.class);
        for(int i = 0; i < ja.size(); i++){
            generatePdfFileByTplConfigStr(tplConfigStr,ja.getJSONObject(i),outputDirPath + File.separator + fileNamePrefix + "_" + (i + 1) + ".pdf");
        }
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfFileByTplAndExcel
     */
    @SneakyThrows
    @Override
    public void generatePdfFileByTplAndExcel(String tplConfigStr, InputStream excelIs, String outputFilePath) {
        JSONArray ja = JSONObject.parseObject(EasyExcelUtils.readWithoutHead(excelIs), JSONArray.class);

        List<ByteArrayOutputStream> bosl = new ArrayList();

        for(int i = 0; i < ja.size(); i++){
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            generatePdfStreamByTplConfigStr(tplConfigStr,ja.getJSONObject(i),os);
            bosl.add(os);
        }
        ByteArrayOutputStream out = this.mergePDF(bosl);
        FileUtil.toFile(new ByteArrayInputStream(out.toByteArray()),new File(outputFilePath));
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfFilesByEleAndExcel
     */
    @SneakyThrows
    @Override
    public void generatePdfFilesByEleAndExcel(String elementStr, InputStream excelIs, String outputDirPath, String fileNamePrefix) {
        JSONArray ja = JSONObject.parseObject(EasyExcelUtils.readWithoutHead(excelIs), JSONArray.class);

        for(int i = 0; i < ja.size(); i++){
            generatePdfFileByHtmlStr(elementStr, ja.getJSONObject(i),outputDirPath + File.separator + fileNamePrefix + "_" + (i + 1)  + ".pdf");
        }
    }

    /**
     * @see ink.rayin.htmladapter.base.PdfGenerator#generatePdfFileByEleAndExcel
     */
    @SneakyThrows
    @Override
    public void generatePdfFileByEleAndExcel(String elementStr, InputStream excelIs, String outputFilePath) {
        JSONArray ja = JSONObject.parseObject(EasyExcelUtils.readWithoutHead(excelIs), JSONArray.class);

        List<ByteArrayOutputStream> bosl = new ArrayList();

        for(int i = 0; i < ja.size(); i++){
            bosl.add(generatePdfSteamByHtmlStrAndData(elementStr, ja.getJSONObject(i)));
        }
        ByteArrayOutputStream out = this.mergePDF(bosl);
        FileUtil.toFile(new ByteArrayInputStream(out.toByteArray()),new File(outputFilePath));
    }


//    /**
//     * 在线导出模板生成方法
//     * @param tplDefStr  模板配置json
//     * @param dataJson   数据json
//     * @param os         输出流
//     * @return
//     * @throws Exception
//     */

//    public RayinMeta generateByTplConfigStrOnlineExport(String tplDefStr, JSONObject dataJson,
//                                                        ByteArrayOutputStream os) throws Exception {
//        String md5Str = tplDefStr.substring(0,32);
//        String base64Str = tplDefStr.substring(32);
//        if(!md5Str.equalsIgnoreCase(DigestUtil.md5Hex(base64Str))){
//            throw new RayinException("模板文件错误，该文件可能不是模板文件!");
//        }
//        String commpressStr = Base64Util.decode(base64Str);
//        if(!commpressStr.substring(0,7).equals("offline")){
//            throw new RayinException("模板文件错误，该文件可能不是模板文件！");
//        }else{
//            commpressStr = commpressStr.substring(7);
//        }
//        String unCommpressStr = GZipUtil.uncompress(commpressStr);
//
//
//        //生成的文件
//        RayinMeta fileInfo = this.generatePdfStreamByTplConfigStr(unCommpressStr, dataJson, os);
//
//        //修改文件属性
//        TemplateConfig pagesConfig = JSONObject.parseObject(unCommpressStr, TemplateConfig.class);
//
//        writePDFAttrs(os,fileInfo,pagesConfig);
//        return fileInfo;
//    }
}
