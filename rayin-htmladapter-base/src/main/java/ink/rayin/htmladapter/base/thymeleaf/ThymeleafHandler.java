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
package ink.rayin.htmladapter.base.thymeleaf;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import ink.rayin.htmladapter.base.utils.CodeMessage;
import ink.rayin.htmladapter.base.utils.RayinException;
import ink.rayin.tools.utils.Charsets;
import ink.rayin.tools.utils.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Map;

/**
 * Thymeleaf 处理类
 * @author Janah Wang / 王柱 2019-08-25
 */

public class ThymeleafHandler {

    private static TemplateEngine templateEngine = new TemplateEngine();
    private static Logger logger = LoggerFactory.getLogger(ThymeleafHandler.class);
    private static ThymeleafHandler thymeleafHandler = new ThymeleafHandler();
    static {
        templateEngine.getConfiguration();
    }

    private ThymeleafHandler (){
    }
    public static ThymeleafHandler getInstance(){
        return thymeleafHandler;
    }

    /**
     * 构件字符串
     * @param htmlStr html字符串
     * @param jsonData json数据
     * @return html字符串
     */
    public String templateEngineProcessByString(String htmlStr, JSONObject jsonData){
         Context context  = new Context();
        if(jsonData != null){
            context.setVariables(JSON.parseObject(jsonData.toJSONString(), Map.class));
        }
        String r = null;
        try{
            r = templateEngine.process(htmlStr, context);
        }catch(Exception e){
            r = e.getCause().toString().replace("org.attoparser.ParseException: Exception evaluating OGNL expression:","The Data paraeter error:");
            logger.debug(r, e);
            throw new RayinException(CodeMessage.DATA_RESOLVE_FAIL, r, e);
        }
        return r;
    }

    /**
     * 模板数据解析
     * @param filePath 文件路径（支持）
     *   1. classpath:
     * 	 2. file:
     * 	 3. ftp:
     * 	 4. http: and https:
     * 	 5. classpath*:
     * 	 6. C:/dir1/ and /Users/lcm
     * @param jsonData json数据
     * @return 解析后的字符串
     */
    public String templateEngineProcessByPath(String filePath, JSONObject jsonData) {
        Context context  = new Context();
        if(jsonData != null){
            context.setVariables(JSON.parseObject(jsonData.toJSONString(),Map.class));
        }

        String r = null;
        try{
            r = templateEngine.process(ResourceUtil.getResourceAsString(filePath, Charsets.UTF_8), context);
        }catch(Exception e){
            r = e.getCause().toString().replace("org.attoparser.ParseException: Exception evaluating OGNL expression:","The Data paraeter error:");
            logger.debug(r);
        }
        return r;
    }
}
