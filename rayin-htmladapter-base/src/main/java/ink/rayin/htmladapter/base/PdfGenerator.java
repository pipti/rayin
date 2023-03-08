/**
 * Copyright (c) 2022-2030, Janah Wang / 王柱 (wangzhu@cityape.tech).
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

import com.alibaba.fastjson2.JSONObject;
import ink.rayin.htmladapter.base.model.tplconfig.RayinMeta;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;

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
 * @author Janah Wang / 王柱
 */
public interface PdfGenerator {
    String defaultFontName = "DFPSongW7";
    String sign = "80550ec4bfc10691e6c45aa88de3f62e";
    /**
     * 根据模板配置文件路径生成pdf
     * Generate pdf based on template configuration file path
     * @param templateLocation 模板配置文件路径
     * @param jsonData  数据json
     * @param outputFilePath  输出文件路径
     * @return RayinMeta 元数据类
     */
    RayinMeta generatePdfFileByTplConfigFile(String templateLocation, JSONObject jsonData,
                                             String outputFilePath);

    /**
     * 根据模板配置字符串生成pdf
     * Generate pdf from template configuration string
     * @param tplConfigStr 模板配置字符串
     * @param jsonData  数据json
     * @param outputFilePath  输出文件路径
     * @return RayinMeta 元数据类
     */
    RayinMeta generatePdfFileByTplConfigStr(String tplConfigStr, JSONObject jsonData,
                                                   String outputFilePath);

    /**
     * 根据模板配置字符串生成带密码的pdf
     * @param tplConfigStr 模板配置字符串
     * @param jsonData  数据json
     * @param os  输出文件路径
     * @param password  密码
     * @return RayinMeta PDF元数据
     */
    RayinMeta generateEncryptPdfStreamByConfigStr(String tplConfigStr, JSONObject jsonData,
                                                  ByteArrayOutputStream os, String password);
    /**
     * 根据模板配置字符串生成PDF流
     * Generate PDF stream based on template configuration string
     * @param tplConfigStr  模板配置json
     * @param jsonData   数据json
     * @param os         输出流
     * @return RayinMeta 元数据类
     */
     RayinMeta generatePdfStreamByTplConfigStr(String tplConfigStr, JSONObject jsonData,
                                               ByteArrayOutputStream os);

    /**
     * 根据单个html文件路径（非配置文件，即html路径）生成pdf至指定路径
     * Generate pdf to the specified path based on a single html file path (non-configuration file, i.e. html path)
     * @param htmlLocation 参见类说明
     * @param jsonData 数据
     * @param outputFilePath 输出文件绝对路径
     * @return boolean true or false
     */
    boolean generatePdfFileByHtmlAndData(String htmlLocation, JSONObject jsonData,
                                         String outputFilePath);

    /**
     * 将html文件与数据匹配生成转换后的字符串
     * Match html file with data to generate converted string
     * @param htmlLocation 参见类说明
     * @param jsonData  数据
     * @return html字符串
     */
     String htmlFileDataFilling(String htmlLocation, JSONObject jsonData);

    /**
     * 将html字符串与数据匹配生成转换后的字符串
     * Match html string with data to generate converted string
     * @param htmlStr html字符串
     * @param jsonData 数据
     * @return html字符串
     */
     String htmlStrDataFilling(String htmlStr, JSONObject jsonData);


    /**
     * html转换为pdf
     * convert html to pdf
     * @param htmlStr html字符串
     * @param outputFile 输出文件路径
     */
     void generatePdfFileByHtmlStr(String htmlStr, String outputFile);

    /**
     * html转换为pdf
     * convert html to pdf
     * @param htmlStr html字符串
     * @param jsonData json数据
     * @param outputFile 输出文件路径
     */
    void generatePdfFileByHtmlStr(String htmlStr, JSONObject jsonData, String outputFile);

    /**
     * html字符串转换为pdf字节流
     * Convert html string to pdf byte stream
     * @param htmlStr html字符串
     * @return PDF pdf输出流
     */
    ByteArrayOutputStream generatePdfStreamByHtmlStr(String htmlStr);

    /**
     * 将带动态脚本的html字符串和数据结合生成pdf字节流
     * Combine html string and data with dynamic script to generate pdf byte stream
     * @param htmlStr html输入流
     * @param jsonData     json数据
     * @return PDF ByteArrayOutputStream
     */
    ByteArrayOutputStream generatePdfSteamByHtmlStrAndData(String htmlStr, JSONObject jsonData);

    /**
     * 将带动态脚本的html文件路径结合数据生成pdf字节流
     * Combine html file path with dynamic script and data to generate pdf byte stream
     * @param htmlLocation  构件路径
     * @param jsonData     json数据
     * @return PDF ByteArrayOutputStream
     */
    ByteArrayOutputStream generatePdfSteamByHtmlFileAndData(String htmlLocation, JSONObject jsonData);


    /**
     * 初始化
     * Initialization
     */
     void init();

    /**
     * 初始化
     * Initialization
     * @param customizeFontPathDirectory 自定义字体加载目录
     */
    void init(String customizeFontPathDirectory);

    /**
     * 初始化-线程池参数
     * Initialization - thread pool parameters
     * @param minIdle 最小线程
     * @param maxIdle 最大空闲
     * @param maxTotal 最大线程总数
     * @param customizeFontPathDirectory 自定义字体目录，可空
     */
    void init(int minIdle,int maxIdle,int maxTotal, String customizeFontPathDirectory);

    /**
     * 获取加载的字体名称
     * Get the loaded font name
     * @return  LinkedHashSet
     */
     LinkedHashSet<String> getFontNames();

    /**
     * excel数据格式，通过模板生成pdf，每条数据一个PDF，并生成至对应的目录中
     * 文件名为fileNamePrefix + "_" + 序号
     * Excel data format, generate pdf through template,
     * one PDF for each piece of data,
     * and generate it to the corresponding directory with the file name fileNamePrefix + "_" + serial number
     * @param tplConfigStr 模板配置串
     * @param excelIs excel文件流
     * @param outputDirPath 输出目录
     * @param fileNamePrefix 文件名前缀
     */
     void generatePdfFilesByTplAndExcel(String tplConfigStr, InputStream excelIs,
                                 String outputDirPath,String fileNamePrefix);

    /**
     * excel数据格式，通过模板生成pdf，并合并生成单个文件
     * Excel data format, generate pdf through template, and merge to generate a single file
     * @param tplConfigStr 模板配置串
     * @param excelIs excel文件流
     * @param outputFilePath 文件路径（带文件名）
     */
    void generatePdfFileByTplAndExcel(String tplConfigStr, InputStream excelIs,
                                      String outputFilePath);

    /**
     * excel数据格式，通过构件生成pdf，每条数据一个PDF，并生成至对应的目录中
     * 文件名为fileNamePrefix + "_" + 序号
     * Excel data format, generate pdf through elements,
     * one PDF for each piece of data,
     * and generate it to the corresponding directory with the file name fileNamePrefix + "_" + serial number
     * @param elementStr 构件字符串
     * @param excelIs excel文件流
     * @param outputDirPath 输出目录
     * @param fileNamePrefix 文件名前缀
     */
    void generatePdfFilesByEleAndExcel(String elementStr, InputStream excelIs,
                                      String outputDirPath,String fileNamePrefix);

    /**
     * excel数据格式，通过构件生成pdf，并生成单个文件
     * excel data format, generate pdf through elements, and generate a single file
     * @param elementStr 构件字符串
     * @param excelIs excel文件流
     * @param outputFilePath 文件路径（带文件名）
     */
    void generatePdfFileByEleAndExcel(String elementStr, InputStream excelIs,
                                      String outputFilePath);
}
