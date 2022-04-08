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
public interface PdfGenerator {
    /**
     * 根据模板配置文件路径生成pdf
     * @param templateLocation 模板配置文件路径
     * @param jsonData  数据json
     * @param outputFilePath  输出文件路径
     * @return RayinMeta 元数据类
     * @throws Exception 抛出异常
     */
    RayinMeta generatePdfFileByTplConfigFile(String templateLocation, JSONObject jsonData,
                                             String outputFilePath) throws Exception;

    /**
     * 根据模板配置字符串生成pdf
     * @param tplConfigStr 模板配置字符串
     * @param jsonData  数据json
     * @param outputFilePath  输出文件路径
     * @return RayinMeta 元数据类
     * @throws Exception 抛出异常
     */
    RayinMeta generatePdfFileByTplConfigStr(String tplConfigStr, JSONObject jsonData,
                                                   String outputFilePath) throws Exception;

    /**
     * 根据模板配置字符串生成带密码的pdf
     * @param configStr 模板配置字符串
     * @param jsonData  数据json
     * @param os  输出文件路径
     * @param password  密码
     * @return RayinMeta PDF元数据
     * @throws Exception 抛出异常
     */
    RayinMeta generateEncryptPdfStreamByConfigStr(String configStr, JSONObject jsonData,
                                                  ByteArrayOutputStream os, String password) throws Exception;
    /**
     * 根据模板配置字符串生成PDF流
     * @param configStr  模板配置json
     * @param data   数据json
     * @param os         输出流
     * @return RayinMeta 元数据类
     * @throws Exception 抛出异常
     */
     RayinMeta generatePdfStreamByTplConfigStr(String configStr, JSONObject data,
                                               ByteArrayOutputStream os) throws Exception;

    /**
     * 根据线上导出模板配置文件生成PDF
     * @param configStr  模板配置json
     * @param dataJson   数据json
     * @param os         输出流
     * @return RayinMeta 元数据类
     * @throws Exception 抛出异常
     */
//    RayinMeta generateByTplConfigStrOnlineExport(String configStr, JSONObject dataJson,
//                                                 ByteArrayOutputStream os) throws Exception;



    /**
     * 根据单个html文件路径（非配置文件，即html路径）生成pdf至指定路径
     * @param htmlLocation 参见类说明
     * @param jsonData
     * @param outputFilePath
     * @return boolean true or false
     * @throws Exception 抛出异常
     */
    boolean generatePdfFileByHtmlAndData(String htmlLocation, JSONObject jsonData,
                                         String outputFilePath) throws Exception;


    /**
     * 根据html文件路径生成字节流
     * @param htmlLocation 构件路径
     * @param jsonData json数据
     * @param pp 页面信息
     * @return PDF ByteArrayOutputStream
     * @throws Exception 抛出异常
     */
    ByteArrayOutputStream generatePdfSteamByHtmlFileAndData(String htmlLocation, JSONObject jsonData, List<HashMap> pp) throws Exception;


    /**
     * 根据模板配置对象生成PDF流
     * @param pagesConfig 模板配置对象
     * @param config 构件对象
     * @param data 数据
     * @param pp 页面信息
     * @return
     * @throws Exception 抛出异常
     */
     //ByteArrayOutputStream generatePdfSteamByHtmlAndData(TemplateConfig pagesConfig, Element config, JSONObject data, List<Element> pp) throws Exception;

    /**
     * 将html文件与数据匹配生成转换后的字符串
     * @param htmlLocation
     * @param variables
     * @return html字符串
     * @throws IOException 抛出异常
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
     * @return html字符串
     * @throws IOException 抛出异常
     */
     String htmlStrDataFilling(String htmlStr, JSONObject variables) throws IOException, WriterException;


    /**
     * html转换为pdf
     * @param htmlStr
     * @param outputFile
     * @throws Exception 抛出异常
     */
     void generatePdfFileByHtmlStr(String htmlStr, String outputFile) throws Exception;

    /**
     * html转换为pdf
     * @param htmlStr
     * @param outputFile
     * @throws Exception 抛出异常
     */
    void generatePdfFileByHtmlStr(String htmlStr, JSONObject variables, String outputFile) throws Exception;

    /**
     * html字符串转换为pdf字节流
     * @param htmlStr
     * @return PDF ByteArrayOutputStream
     * @throws Exception 抛出异常
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
     * @return 元数据HashMap
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException 抛出异常
     * 2020-01-07
     */
     HashMap<String, String> pdfAttrsRead(InputStream pdf) throws IOException, ParserConfigurationException, SAXException;

    /**
     * PDF 模板元数据读取
     * @param pdf
     * @return json 字符串
     * @throws IOException 抛出异常
     * @throws ParserConfigurationException 抛出异常
     * @throws SAXException 抛出异常
     * 2020-01-07
     */
     String pdfPageInfoRead(InputStream pdf) throws ParserConfigurationException, SAXException, IOException;

    /**
     * 根据html文件路径生成pdf字节流
     * @param htmlLocation  构件路径
     * @param data     json数据
     * @return PDF ByteArrayOutputStream
     * @throws Exception 抛出异常
     */
     ByteArrayOutputStream generatePdfSteamByHtmlFileAndData(String htmlLocation, JSONObject data) throws Exception;

    /**
     * 根据html文件流（html）生成pdf字节流
     * @param htmlInputStream html输入流
     * @param data     json数据
     * @return PDF ByteArrayOutputStream
     * @throws Exception 抛出异常
     */
     ByteArrayOutputStream generatePdfSteamByHtmlStreamAndData(InputStream htmlInputStream, JSONObject data) throws Exception;

    /**
     * 根据html文件流（html）生成pdf字节流
     * @param htmlStr html输入流
     * @param data     json数据
     * @return PDF ByteArrayOutputStream
     * @throws Exception 抛出异常
     */
    ByteArrayOutputStream generatePdfSteamByHtmlStrAndData(String htmlStr, JSONObject data) throws Exception;

    /**
     * 添加图片
     * @param inputStream
     * @param imageByte
     * @param page
     * @param x
     * @param y
     * @param width
     * @param height
     * @return PDF ByteArrayOutputStream
     * @throws Exception 抛出异常
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
     * @return PDF ByteArrayOutputStream
     * @throws Exception 抛出异常
     */
     ByteArrayOutputStream addImage(InputStream inputStream, ByteArrayOutputStream out,byte[] imageByte, int page,float x,float y,float width, float height) throws Exception;

    /**
     * 初始化
     * @throws Exception 抛出异常
     */
     void init() throws Exception;

    /**
     * 初始化
     * @param customizeFontPathDirectory 自定义字体加载目录
     * @throws Exception 抛出异常
     */
    void init(String customizeFontPathDirectory) throws Exception;

    /**
     * 初始化-线程池参数
     * @param minIdle 最小线程
     * @param maxIdle 最大空闲
     * @param maxTotal 最大线程总数
     * @param customizeFontPathDirectory 自定义字体目录，可空
     * @throws Exception
     */
    void init(int minIdle,int maxIdle,int maxTotal, String customizeFontPathDirectory) throws Exception;

    /**
     * 获取加载的字体名称
     * @return  LinkedHashSet
     */
     LinkedHashSet<String> getFontNames();

    /**
     * excel数据导入生成pdf，并生成至对应的目录中
     * 文件名为fileNamePrefix + "_" + 序号
     * @param tplConfigStr 模板配置串
     * @param excelIs excel文件流
     * @param outputDirPath 输出目录
     * @param fileNamePrefix 文件名前缀
     */
     void generatePdfFilesByTplAndExcel(String tplConfigStr, InputStream excelIs,
                                 String outputDirPath,String fileNamePrefix) throws Exception;

    /**
     * excel数据导入生成pdf，并生成单个文件
     *
     * @param tplConfigStr 模板配置串
     * @param excelIs excel文件流
     * @param outputFilePath 文件路径（带文件名）
     */
    void generatePdfFileByTplAndExcel(String tplConfigStr, InputStream excelIs,
                                      String outputFilePath) throws Exception;

    /**
     * excel数据导入生成pdf，并生成至对应的目录中
     * 文件名为fileNamePrefix + "_" + 序号
     * @param elementStr 构件字符串
     * @param excelIs excel文件流
     * @param outputDirPath 输出目录
     * @param fileNamePrefix 文件名前缀
     */
    void generatePdfFilesByEleAndExcel(String elementStr, InputStream excelIs,
                                      String outputDirPath,String fileNamePrefix) throws Exception;

    /**
     * excel数据导入生成pdf，并生成单个文件
     *
     * @param elementStr 构件字符串
     * @param excelIs excel文件流
     * @param outputFilePath 文件路径（带文件名）
     */
    void generatePdfFileByEleAndExcel(String elementStr, InputStream excelIs,
                                      String outputFilePath) throws Exception;

    /**
     * excel数据导入生成pdf，并生成压缩包
     *
     * @param elementStr 构件字符串
     * @param excelIs excel文件流
     * @param outputFilePath 压缩包文件路径（带文件名）
     */
    void generatePdfFilesZipByEleAndExcel(String elementStr, InputStream excelIs,
                                      String outputFilePath);

    /**
     * excel数据导入生成pdf，并生成压缩包
     *
     * @param tplConfigStr 模板配置串
     * @param excelIs excel文件流
     * @param outputFilePath 压缩包文件路径（带文件名）
     */
    void generatePdfFilesZipByTplAndExcel(String tplConfigStr, InputStream excelIs,
                                          String outputFilePath);
}
