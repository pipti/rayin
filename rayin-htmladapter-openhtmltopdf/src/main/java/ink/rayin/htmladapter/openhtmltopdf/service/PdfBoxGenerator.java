/**
 * Copyright (c) 2022-2030, Janah wz 王柱 (carefreefly@163.com).
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

import com.alibaba.fastjson.JSONObject;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.base.model.tplconfig.*;
import ink.rayin.htmladapter.base.thymeleaf.ThymeleafHandler;
import ink.rayin.htmladapter.openhtmltopdf.factory.OpenhttptopdfRenderBuilder;
import ink.rayin.htmladapter.openhtmltopdf.factory.OpenhttptopdfRendererObjectFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.zxing.WriterException;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import ink.rayin.htmladapter.openhtmltopdf.utils.PdfBoxPositionFindByKey;
import ink.rayin.tools.utils.*;
import ink.rayin.htmladapter.base.utils.JsonSchemaValidator;
import ink.rayin.htmladapter.base.utils.RayinException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.encryption.*;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.util.*;
import java.util.List;
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

    public PdfBoxGenerator() throws IOException {
            jsonSchemaNode = JsonSchemaValidator.getJsonNodeFromInputStream(ResourceUtil.getResourceAsStream(jsonSchema));

            if(jsonSchemaNode == null){
                new RayinException("json校验文件不存在！初始化失败");
            }

        //}
    }

    @Override
    public void init() throws Exception {
        OpenhttptopdfRendererObjectFactory.init();
    }

    @Override
    public void init(String customizeFontPathDirectory) throws Exception {
        OpenhttptopdfRendererObjectFactory.init(customizeFontPathDirectory);
    }

    /**
     * 初始化-线程池参数
     * @param minIdle 最小线程
     * @param maxIdle 最大空闲
     * @param maxTotal 最大线程总数
     * @param customizeFontPathDirectory 自定义字体目录，可空
     * @throws Exception
     */
    @Override
    public void init(int minIdle, int maxIdle, int maxTotal, String customizeFontPathDirectory) throws Exception {
        OpenhttptopdfRendererObjectFactory.init(minIdle, maxIdle, maxTotal, customizeFontPathDirectory);
    }

    /**
     * 根据模板配置文件路径生成pdf
     * @param templateLocation 模板配置json文件路径
     * @param jsonData  数据json
     * @param outputFilePath  输出文件路径
     * @return
     * @throws Exception
     */
    @Override
    public RayinMeta generatePdfFileByTplConfigFile(String templateLocation, JSONObject jsonData,
                                                    String outputFilePath) throws Exception {

        //通过path获取配置json
        JsonNode tplConfigJsonDataNode = JsonSchemaValidator.getJsonNodeFromString(ResourceUtil.getResourceAsString(templateLocation, Charsets.UTF_8));

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

    @Override
    public RayinMeta generateEncryptPdfStreamByConfigStr(String tplStr, JSONObject jsonData,
                                                         ByteArrayOutputStream os, String password) throws Exception {

        //生成的文件
        RayinMeta fileInfo = generatePdfStreamByTplConfigStr(tplStr,jsonData,os);

        //修改文件属性
        TemplateConfig pagesConfig = JSONObject.parseObject(tplStr, TemplateConfig.class);
        if (StringUtil.isNotBlank(password)) {
            fileInfo.setSecretKey(password);
        }
        writePDFAttrs(os,fileInfo,pagesConfig);
        return fileInfo;

    }
    /**
     * 根据模板配置文件生成PDF
     * @param tplStr  模板配置json
     * @param dataJson   数据json
     * @param os         输出流
     * @return
     * @throws Exception
     */
    @Override
    public RayinMeta generatePdfStreamByTplConfigStr(String tplStr, JSONObject dataJson,
                                                     ByteArrayOutputStream os) throws Exception {
        /** 校验配置开始 **/
        //TODO 获取tpl_code配置  初始化项目时校验 根据所有tplcode配置校验配置是否正确



        JsonNode tplConfigJsonDataNode = JsonSchemaValidator.getJsonNodeFromString(tplStr);
        if(tplConfigJsonDataNode == null){
            new RayinException("json报文格式不正确！");
        }
        JsonSchemaValidator.validateJsonByFgeByJsonNode(tplConfigJsonDataNode, jsonSchemaNode);
        /** 校验配置结束 **/

        /** 生成文件开始 **/
        //依据构建配置生成PDF
        TemplateConfig pagesConfig = JSONObject.parseObject(tplStr, TemplateConfig.class);
        //遍历配置
        List<Element> pages = pagesConfig.getElements();
        List<PageNumDisplayPos> templatePageNumDisplayPos = null;
        if(pagesConfig.getPageNumDisplayPoss() != null && pagesConfig.getPageNumDisplayPoss().size() > 0){
            templatePageNumDisplayPos = pagesConfig.getPageNumDisplayPoss();
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
            ByteArrayOutputStream tmp = generatePdfSteamByHtmlAndData(pagesConfig,el,dataJson,pageProperties);
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
                                        if (pos.getX() != 0 && pos.getY() != 0) {
                                            if (StringUtil.isNotBlank(pos.getContent())) {
                                                try {
                                                    String pageNumContent = pos.getContent().replace("${pageNum}", pageNum + "").replace("${pageNumTotal}", pageNumTotal + "");
                                                    PDPageContentStream contentStream
                                                            = new PDPageContentStream(doc, content, PDPageContentStream.AppendMode.PREPEND, false);

                                                    contentStream.beginText();
                                                    contentStream.newLineAtOffset(pos.getX(), overContentHeight - pos.getY());

                                                    contentStream.setFont(PDType0Font.load(doc, OpenhttptopdfRendererObjectFactory.getFSSupplierCacheCache().get(StringUtil.isNotBlank(pos.getFontFamily())?pos.getFontFamily():"FangSong").supply()), pos.getFontSize() != 0?pos.getFontSize():10);

                                                    contentStream.showText(pageNumContent);
                                                    contentStream.endText();
                                                    contentStream.close();

                                                } catch (DOMException e) {
                                                    throw new RayinException("页码html标签格式设置错误！");
                                                }

                                            } else {
                                                PDPageContentStream contentStream
                                                        = new PDPageContentStream(doc, content, PDPageContentStream.AppendMode.PREPEND, false);
                                                contentStream.beginText();
                                                contentStream.newLineAtOffset(pos.getX(), overContentHeight - pos.getY());

                                                contentStream.setFont(PDType0Font.load(doc, OpenhttptopdfRendererObjectFactory.getFSSupplierCacheCache().get("FangSong").supply()), 10);
                                                contentStream.showText(footerStr);
                                                contentStream.endText();
                                                contentStream.close();
                                            }
                                        }
                                        
                                        //全局页码设置获取页码坐标
                                        if (templatePageNumDisplayPos != null){
                                            for (PageNumDisplayPos posTpl : templatePageNumDisplayPos) {
                                                // 坐标页码设置
                                                if (posTpl.getX() != 0 && posTpl.getY() != 0) {
                                                    if (StringUtil.isNotBlank(posTpl.getContent())) {

                                                        try {
                                                            String pageNumContent = pos.getContent().replace("${pageNum}", pageNum + "").replace("${pageNumTotal}", pageNumTotal + "");
                                                            PDPageContentStream contentStream =
                                                                    new PDPageContentStream(doc, content, PDPageContentStream.AppendMode.PREPEND, false);

                                                            contentStream.beginText();
                                                            contentStream.newLineAtOffset(pos.getX(), overContentHeight - pos.getY());

                                                            contentStream.setFont(PDType0Font.load(doc, OpenhttptopdfRendererObjectFactory.getFSSupplierCacheCache().get(StringUtil.isNotBlank(pos.getFontFamily())?pos.getFontFamily():"FangSong").supply()), pos.getFontSize() != 0?pos.getFontSize():10);

                                                            contentStream.showText(pageNumContent);
                                                            contentStream.endText();
                                                            contentStream.close();

                                                        } catch (DOMException e) {
                                                            throw new RayinException("页码html标签格式设置错误！");
                                                        }

                                                    } else {
                                                        PDPageContentStream contentStream =
                                                                new PDPageContentStream(doc, content, PDPageContentStream.AppendMode.PREPEND, false);
                                                        contentStream.beginText();
                                                        contentStream.newLineAtOffset(posTpl.getX(), overContentHeight - posTpl.getY());

                                                        contentStream.setFont(PDType0Font.load(doc, OpenhttptopdfRendererObjectFactory.getFSSupplierCacheCache().get("FangSong").supply()), 10);
                                                        contentStream.showText(footerStr);
                                                        contentStream.endText();
                                                        contentStream.close();
                                                    }
                                                }
                                            }
                                        }



                                        // 关键字页码设置
                                        if(StringUtil.isNotBlank(pos.getMark())){

                                            List<float[]> fl = PdfBoxPositionFindByKey.findKeywordPagePostions(out.get(i).toByteArray(),pos.getMark(),j);

                                            if(fl.size() > 0){

                                                PDPageContentStream contentStream = new PDPageContentStream(doc, content,PDPageContentStream.AppendMode.PREPEND, false);
                                                contentStream.beginText();
                                                for(float[] f:fl){
                                                contentStream.newLineAtOffset(f[0], f[1]);
                                                }
                                                contentStream.setFont(PDType0Font.load(doc, OpenhttptopdfRendererObjectFactory.getFSSupplierCacheCache().get("FangSong").supply()), 10);
                                                contentStream.showText(footerStr);
                                                contentStream.endText();
                                                contentStream.close();
                                            }

                                        }


                                    }
                                }else {
                                    PDPageContentStream contentStream = new PDPageContentStream(doc, content,PDPageContentStream.AppendMode.PREPEND, false);
                                    contentStream.beginText();
                                    contentStream.newLineAtOffset(content.getMediaBox().getWidth()/2 - 30, 30);
                                    contentStream.setFont(PDType0Font.load(doc, OpenhttptopdfRendererObjectFactory.getFSSupplierCacheCache().get("FangSong").supply()), 10);
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
//            pa.setQualityTestType(page.getQualityTestType());
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
     * 根据单个tpl生成pdf
     * @param templateLocation 模板文件路径
     * @param jsonObject 需要合成的json数据
     * @param outputFilePath 输出pdf路径
     * @return
     * @throws Exception
     */
    @Override
    public boolean generatePdfFileByHtmlAndData(String templateLocation, JSONObject jsonObject,
                                                String outputFilePath) throws Exception {

        String htmlContent = htmlFileDataFilling(templateLocation,
                jsonObject);

        //logger.debug(htmlContent);
        this.generatePdfFileByHtmlStr(htmlContent, outputFilePath);

        return true;
    }

    /**
     * 根据模板tpl生成字节流
     * @param templateLocation 模板路径
     * @param jsonData 动态数据
     * @return
     * @throws Exception
     */
    @Override
    public ByteArrayOutputStream generatePdfSteamByHtmlAndData(String templateLocation, JSONObject jsonData, List<HashMap> pp) throws Exception {
        Map<String, Object> variables;

        String htmlContent = htmlFileDataFilling(templateLocation, jsonData);

        logger.info(htmlContent);
        return this.generatePdfStreamByHtmlStr(htmlContent);
    }

    /**
     * 根据tpl生成字节流
     * @param templateIs    模板文件流
     * @param jsonData      需要合成的json数据
     * @return
     * @throws Exception
     */
    @Override
    public ByteArrayOutputStream generatePdfSteamByHtmlAndData(InputStream templateIs, JSONObject jsonData) throws Exception {
        StringBuffer t = new StringBuffer(500);
        BufferedReader br = null;
        Reader reader = new InputStreamReader(templateIs);
        String data = null;

        br = new BufferedReader(reader);
        while ((data = br.readLine()) != null) {
            t.append(data);
        }

        String htmlContent = htmlStrDataFilling(t.toString(),
                jsonData);
        //logger.info(htmlContent);
        return this.generatePdfStreamByHtmlStr(htmlContent);
    }

    /**
     * 根据模板配置生成PDF流
     * @param pagesConfig
     * @param config
     * @param data
     * @param pp
     * @return
     * @throws Exception
     */
    private ByteArrayOutputStream generatePdfSteamByHtmlAndData(TemplateConfig pagesConfig, Element config, JSONObject data, List<Element> pp) throws Exception {

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
                    blankOS = generatePdfSteamByHtmlAndData(config.getBlankElementPath(),data);
                }else{
                    if(StringUtil.isNotBlank(pagesConfig.getBlankElementPath())){
                        blankOS = generatePdfSteamByHtmlAndData(pagesConfig.getBlankElementPath(),data);
                    }else {
                        //如果没有配置获取默认样式
                        blankOS = generatePdfSteamByHtmlAndData(ResourceUtil.getResourceAsStream("blank.html"), data);
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
     * 将模板与数据匹配生成转换后的字符串
     * @param htmlLocation html路径
     * @param data json数据
     * @return
     * @throws IOException
     */
    @Override
    public String htmlFileDataFilling(String htmlLocation, JSONObject data) throws IOException, WriterException {
        String htmlContent = "";
        try{
            htmlContent = thymeleafHandler.templateEngineProcessByPath(htmlLocation, data);

        }finally{

        }
        return htmlContent;
    }

    /**
     * 将模板与数据匹配生成转换后的字符串
     * @param htmlStr html字符串
     * @param data json数据
     * @return
     * @throws IOException
     */
    @Override
    public String htmlStrDataFilling(String htmlStr, JSONObject data) throws IOException, WriterException {
        String htmlContent = "";
        if(StringUtil.isBlank(htmlStr)) return "";

        try{
            htmlContent = thymeleafHandler.templateEngineProcessByString(htmlStr, data);

        }finally{

        }
        return htmlContent;
    }

    /**
     * html转换为pdf 文件
     * @param htmlStr html字符串
     * @param outputFile pdf文件路径
     * @throws Exception
     */
    @Override
    public void generatePdfFileByHtmlStr(String htmlStr, String outputFile)
            throws Exception {

        File f = new File(outputFile);
        if (f != null && !f.getParentFile().exists()) {
            f.getParentFile().mkdir();
        }
        FileUtils.writeByteArrayToFile(new File(outputFile), generatePdfStreamByHtmlStr(htmlStr).toByteArray());
    }

    @Override
    public ByteArrayOutputStream generatePdfStreamByHtmlStr(String htmlContent) throws Exception{
        return generatePdfStreamByHtmlStr(htmlContent, null);
    }

    /**
     * html转换为pdf字节流
     * @param htmlContent
     * @return
     * @throws Exception
     */

    private ByteArrayOutputStream generatePdfStreamByHtmlStr(String htmlContent, Set<MarkInfo> markKeys)
            throws Exception {
        //解析是否存在embed标签，并对其进行解析
        List<ByteArrayOutputStream> apendFiles =  new ArrayList<>();

        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(htmlContent);

        Elements imgLinks = htmlDoc.getElementsByTag("img");
        for(org.jsoup.nodes.Element link : imgLinks){
            String src = link.attr("src");
            if(StringUtil.isNotBlank(src)){
                if(src.startsWith("/") || src.startsWith("\\")){
                    link.attr("src","file:" + src);
                }
            }
        }
        Elements objectLinks = htmlDoc.getElementsByTag("object");
        //Elements divLinks = htmlDoc.getElementsByTag("object");
        for (org.jsoup.nodes.Element link : objectLinks) {
            String inner = link.text();
            String type = link.attr("type");
            String value = link.attr("value");

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
                        try{
                            pdfOs = ResourceUtil.getResourceAsByte(value);
                        }catch (FileNotFoundException fe){
                            throw new RayinException("无法找到文件：" + fe.getMessage());
                        }


                        PDDocument doc = PDDocument.load(pdfOs.toByteArray());

                        int elPageNum = doc.getNumberOfPages();

                        if (value.startsWith("http://") || value.startsWith("https://") || value.startsWith("file:")) {

                        }else if (value.startsWith("/") || value.startsWith("\\")) {
                            value = "file:" + value;
                        }else{
                            value = "file:" + ResourceUtil.getResourceAbsolutePathByClassPath(value);
                        }
                        for(int i = 1; i <= elPageNum; i++){
                            //content = doc.getPage(i - 1);
                            //overContentWidth = content.getMediaBox().getWidth();
                            box.append("<img width=\"100%\" src=\""+ value +"\" page=\""+ i + "\"/>\n");
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
                                    // width = Float.parseFloat(st.replaceAll("[a-zA-Z:; ]",""));
                                    continue;
                                }
                                if(st.indexOf("height:") >= 0){
                                    height = DisplayMeasureConvert.webMeasureToPDFPoint(st.toLowerCase().replace("height:",""));
                                    //height = Float.parseFloat(st.replaceAll("[a-zA-Z:; ]",""));
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
                }

            }

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
                OpenhttptopdfRendererObjectFactory.returnPdfBoxRenderer(openhttptopdfRenderBuilder);
                throw e;
            }

        } catch (Exception e) {
            OpenhttptopdfRendererObjectFactory.returnPdfBoxRenderer(openhttptopdfRenderBuilder);
            throw e;
        } finally {
            OpenhttptopdfRendererObjectFactory.returnPdfBoxRenderer(openhttptopdfRenderBuilder);
        }

        PDDocument pdDoc = PDDocument.load(out.toByteArray());
        PDDocumentInformation pdInfo = pdDoc.getDocumentInformation();
        pdInfo.setProducer("@MADE BY RAYIN");

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
                t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,
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
     * @param targetFile
     * @param outByte
     * @return
     * @throws Exception
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
    @Override
    public ByteArrayOutputStream addImage(InputStream inputStream,byte[] imageByte,
                                          int page,float x,float y,float width, float height) throws Exception {
        return addImage(inputStream,null, imageByte,page,x,y,width,height);
    }

    /**
     * 为文档添加一个图片
     * @param inputStream 文档流
     * @param out 文档流
     * @param imageByte 图片
     * @param page 目标页
     * @param x x坐标
     * @param y y坐标
     * @param width 宽
     * @param height 高
     * @return
     * @throws Exception
     */
    @Override
    public ByteArrayOutputStream addImage(InputStream inputStream, ByteArrayOutputStream out,byte[] imageByte,
                                          int page,float x,float y,float width, float height)
            throws Exception {

//        PdfReader pdfReader = new PdfReader(inputStream);
//        if (out == null) {
//            out = new ByteArrayOutputStream();
//        }
//        PdfStamper stamp = new PdfStamper(pdfReader, out);
//
//        //设置图片位置与大小
//        Image image = Image.getInstance(imageByte);
//        image.setAbsolutePosition(x,y);
//        image.scaleAbsoluteHeight(height);
//        image.scaleAbsoluteWidth(width);
//
//        //在指定页添加图片
//        PdfContentByte content = stamp.getUnderContent(page);
//        content.addImage(image);
//        stamp.close();
//        pdfReader.close();
//        return out;
        return null;
    }
    /**
     * 将字节流生成文件
     * @param os 输出流
     * @param outByte 待生成文件byte
     * @return
     * @throws Exception
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
     * @param os
     * @param epfileInfo
     * @param pagesConfig
     * @return
     * @throws Exception
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
//        info.setProducer(StringUtil.isBlank(pagesConfig.getProducer())?
//                UnicodeUtil.decode("\\u0040\\u004d\\u0041\\u0044\\u0045\\u0020\\u0042\\u0059\\u0020\\u0052\\u0041\\u0059\\u0049\\u004e"):pagesConfig.getProducer());
        info.setProducer(
                UnicodeUtil.decode("\\u0040\\u004d\\u0041\\u0044\\u0045\\u0020\\u0042\\u0059\\u0020\\u0052\\u0041\\u0059\\u0049\\u004e"));



        info.setCreator(pagesConfig.getCreator());


        //写入文件相关配置信息包括页码，单模板类型以及页码起始页
        final Base64.Encoder encoder = Base64.getEncoder();
        logger.info(JSONObject.toJSONString(epfileInfo));
        info.setCustomMetadataValue("PagesInfo",encoder.encodeToString(JSONObject.toJSONString(epfileInfo).getBytes()));

       // ByteArrayOutputStream baos = new ByteArrayOutputStream();
        os.reset();
        doc.save(os);
        doc.close();
       // return baos;
    }


    /**
     * PDF 模板元数据读取
     * @param fis
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * 2020-01-07
     */
    @Override
    public HashMap<String, String> pdfAttrsRead(InputStream fis) throws IOException, ParserConfigurationException, SAXException {
        // 读取要合并的文档
        PDDocument document = PDDocument.load(fis);
        PDDocumentInformation info = document.getDocumentInformation();

        HashMap pdfMeta = new HashMap<String,String>();
        pdfMeta.put("Author",info.getAuthor());
        pdfMeta.put("Creator",info.getCreator());
        pdfMeta.put("Keywords",info.getKeywords());
        pdfMeta.put("Producer",info.getProducer());
        pdfMeta.put("Subject",info.getSubject());
        pdfMeta.put("Title",info.getTitle());
        pdfMeta.put("PagesInfo",info.getCustomMetadataValue("PagesInfo"));
        document.close();
        return pdfMeta;
    }

    /**
     * PDF 文件信息读取
     * @param fis
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * 2020-01-07
     */
    @Override
    public String pdfPageInfoRead(InputStream fis) throws ParserConfigurationException, SAXException, IOException {
        HashMap pra = pdfAttrsRead(fis);
        //写入文件相关配置信息包括页码，单模板类型以及页码起始页
        final Base64.Decoder decoder = Base64.getDecoder();
        Object pagesInfo = pra.get("PagesInfo");
        if (pagesInfo == null) {
            return null;
        }
        return new String(decoder.decode(pagesInfo.toString()),"UTF-8") ;
    }

    /**
     * 根据tpl生成字节流
     * @param templatePath  模板路径
     * @param jsonData      json数据
     * @return
     * @throws Exception
     */
    @Override
    public ByteArrayOutputStream generatePdfSteamByHtmlAndData(String templatePath, JSONObject jsonData) throws Exception {

        String htmlContent = htmlFileDataFilling(templatePath,
                jsonData);
        //logger.info(htmlContent);
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
        //System.out.println(pbts.SubStringIgnoreHtml(html,1,15));

    }


    private int calText(Node n,int offset,int beginIndex,int endIndex){
//        if(offset >= endIndex){
//            return -1;
//        }
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
                    //offset = beginIndex;
                    if (offset < beginIndex && n instanceof TextNode) {
                        //System.out.println("text:" + tmp.substring(beginIndex - offset, nodelen));
                        //n = new TextNode(n.toString().substring(beginIndex - offset, n.toString().length()));
                        ((TextNode) n).text(tmp.substring(beginIndex - offset, nodelen));
                        // b.add(new TextNode(n.toString().substring(beginIndex - offset, n.toString().length())));
                    } else {
                        // n = new TextNode(n.toString().substring(beginIndex - offset, n.toString().length()));
                        //System.out.println("text:" + tmp);
                        //b.add(n);
                        if(n instanceof TextNode){
                            ((TextNode) n).text(tmp);
                        }

                    }
                    offset = offset + nodelen;
                    return offset;
                }

                if ((offset + nodelen) >= endIndex && offset < endIndex) {
//                    if (offset < beginIndex) {
//                        System.out.println("text:" + tmp.substring(beginIndex - offset, endIndex - offset));
//                        ((TextNode) n).text(tmp.substring(beginIndex - offset, endIndex - offset));
//                        offset = offset + nodelen;
//                    } else
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
            //int a = 0;

            List<Node> ln = n.childNodes();
            for (int i = 0; i < ln.size(); i++) {

//                if(offset > endIndex){
//                    ln.get(i).remove();
//                    i--;
//                    continue;
//                }
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








//                if(offset >= beginIndex && offset <= endIndex){
//
//
//                }
            }

//            for(Node nc:n.childNodes()){
//                if(offset == -1 || offset == endIndex){
//                    nc.remove();
//                    continue;
//                    //((TextNode) nc).text("");
//                    //return offset;
//                }
//
//                if(offset < endIndex && offset != -1){
//                    offset = calText(nc,offset,beginIndex,endIndex);
//                }
//                a++;
//
//            }
            return offset;
        }
        return offset;
    }


    //截取数字
    public String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    @Override
    public LinkedHashSet<String> getFontNames(){
        return OpenhttptopdfRendererObjectFactory.getFontNames();
    }


    /**
     * 在线导出模板生成方法
     * @param tplDefStr  模板配置json
     * @param dataJson   数据json
     * @param os         输出流
     * @return
     * @throws Exception
     */

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
