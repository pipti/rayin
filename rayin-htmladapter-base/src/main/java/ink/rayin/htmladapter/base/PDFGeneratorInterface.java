package ink.rayin.htmladapter.base;

import com.alibaba.fastjson.JSONObject;
import ink.rayin.htmladapter.base.model.tplconfig.RayinMeta;
import com.google.zxing.WriterException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * PDF生成接口
 *
 * 方法中htmlLocation,templateLocation参数支持的协议如下：
 * <p>
 * 1. classpath:
 * 2. file:
 * 3. ftp:
 * 4. http: and https:
 * 5. classpath*:
 * 6. C:/dir1/ and /Users/lcm
 * </p>
 * @author Jonah wz
 */
public interface PDFGeneratorInterface {
    /**
     * 根据模板配置文件路径生成pdf
     * @param templateLocation 模板配置文件路径
     * @param jsonData  数据json
     * @param outputFilePath  输出文件路径
     * @return
     * @throws Exception
     */
    RayinMeta generatePdfFileByTplConfigFile(String templateLocation, JSONObject jsonData,
                                             String outputFilePath) throws Exception;

    /**
     * 根据模板配置字符串生成带密码的pdf
     * @param configStr 模板配置字符串
     * @param jsonData  数据json
     * @param os  输出文件路径
     * @param password  密码
     * @return RayinMeta PDF元数据
     * @throws Exception
     */
    RayinMeta generateEncryptPdfStreamByConfigStr(String configStr, JSONObject jsonData,
                                                  ByteArrayOutputStream os, String password) throws Exception;
    /**
     * 根据模板配置字符串生成PDF流
     * @param configStr  模板配置json
     * @param data   数据json
     * @param os         输出流
     * @return
     * @throws Exception
     */
     RayinMeta generatePdfStreamByTplConfigStr(String configStr, JSONObject data,
                                               ByteArrayOutputStream os) throws Exception;

    /**
     * 根据线上导出模板配置文件生成PDF
     * @param configStr  模板配置json
     * @param dataJson   数据json
     * @param os         输出流
     * @return
     * @throws Exception
     */
//    RayinMeta generateByTplConfigStrOnlineExport(String configStr, JSONObject dataJson,
//                                                 ByteArrayOutputStream os) throws Exception;



    /**
     * 根据单个html文件路径（非配置文件，即html路径）生成pdf至指定路径
     * @param htmlLocation 参见类说明
     * @param jsonData
     * @param outputFilePath
     * @return
     * @throws Exception
     */
    boolean generatePdfFileByHtmlAndData(String htmlLocation, JSONObject jsonData,
                                         String outputFilePath) throws Exception;


    /**
     * 根据html文件路径生成字节流
     * @param htmlLocation 构件路径
     * @param jsonData json数据
     * @param pp 页面信息
     * @return
     * @throws Exception
     */
    ByteArrayOutputStream generatePdfSteamByHtmlAndData(String htmlLocation, JSONObject jsonData, List<HashMap> pp) throws Exception;


    /**
     * 根据模板配置对象生成PDF流
     * @param pagesConfig 模板配置对象
     * @param config 构件对象
     * @param data 数据
     * @param pp 页面信息
     * @return
     * @throws Exception
     */
     //ByteArrayOutputStream generatePdfSteamByHtmlAndData(TemplateConfig pagesConfig, Element config, JSONObject data, List<Element> pp) throws Exception;

    /**
     * 将html文件与数据匹配生成转换后的字符串
     * @param htmlLocation
     * @param variables
     * @return
     * @throws IOException
     */
     String htmlFileDataFilling(String htmlLocation, JSONObject variables) throws IOException, WriterException;

//    /**
//     * 将构件与数据匹配生成转换后的字符串
//     * @param eleContentStr
//     * @param variables
//     * @return
//     * @throws IOException
//     */
//     String eleConvertContentByContentStr(String eleContentStr, JSONObject variables) throws IOException, WriterException;

    /**
     * 将html字符串与数据匹配生成转换后的字符串
     * @param htmlStr html字符串
     * @param variables 数据
     * @return
     * @throws IOException
     */
     String htmlStrDataFilling(String htmlStr, JSONObject variables) throws IOException, WriterException;


    /**
     * html转换为pdf
     * @param htmlStr
     * @param outputFile
     * @throws Exception
     */
     void generatePdfFileByHtmlStr(String htmlStr, String outputFile) throws Exception;

    /**
     * html字符串转换为pdf字节流
     * @param htmlStr
     * @return
     * @throws Exception
     */
    ByteArrayOutputStream generatePdfStreamByHtmlStr(String htmlStr) throws Exception;

//    /**
//     * html转换为pdf字节流
//     * @param htmlContent
//     * @return
//     * @throws Exception
//     */
//     ByteArrayOutputStream generatePdfStreamHtml(String htmlContent, Set<MarkInfo> signatureKeys)
//            throws Exception;



//    /**
//     * 将字节流生成文件
//     * @param targetFile
//     * @param outByte
//     * @return
//     * @throws Exception
//     */
//     RayinMeta writeTargetFileByByte(String targetFile, byte[] outByte)
//            throws Exception;


    /**
     * PDF 元数据读取
     * @param pdf pdf输入流
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @date 2020-01-07
     */
     HashMap<String, String> pdfAttrsRead(InputStream pdf) throws IOException, ParserConfigurationException, SAXException;

    /**
     * PDF 模板元数据读取
     * @param pdf
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @date 2020-01-07
     */
     String pdfPageInfoRead(InputStream pdf) throws ParserConfigurationException, SAXException, IOException;

    /**
     * 根据html文件路径生成pdf字节流
     * @param htmlLocation  构件路径
     * @param data     json数据
     * @return
     * @throws Exception
     */
     ByteArrayOutputStream generatePdfSteamByHtmlAndData(String htmlLocation, JSONObject data) throws Exception;

    /**
     * 根据html文件流（html）生成pdf字节流
     * @param htmlInputStream html输入流
     * @param data     json数据
     * @return
     * @throws Exception
     */
     ByteArrayOutputStream generatePdfSteamByHtmlAndData(InputStream htmlInputStream, JSONObject data) throws Exception;

    /**
     * 添加图片
     * @param inputStream
     * @param imageByte
     * @param page
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     * @throws Exception
     */
     ByteArrayOutputStream addImage(InputStream inputStream,byte[] imageByte, int page,float x,float y,float width, float height) throws Exception;

    /**
     * 添加图片
     * @param inputStream
     * @param out
     * @param imageByte
     * @param page
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     * @throws Exception
     */
     ByteArrayOutputStream addImage(InputStream inputStream, ByteArrayOutputStream out,byte[] imageByte, int page,float x,float y,float width, float height) throws Exception;

    /**
     * 默认初始化
     * @throws Exception
     */
     void init() throws Exception;

    /**
     * 获取加载的字体名称
     * @return
     */
     LinkedHashSet<String> getFontNames();
}
