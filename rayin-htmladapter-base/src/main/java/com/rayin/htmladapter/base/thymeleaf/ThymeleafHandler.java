package com.rayin.htmladapter.base.thymeleaf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.google.gson.Gson;
import com.rayin.tools.utils.Charsets;
import com.rayin.tools.utils.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Map;

/**
 * Thymeleaf 处理类
 * @author Jonah wz
 */

public class ThymeleafHandler {

    private static TemplateEngine templateEngine = new TemplateEngine();
    private static Context context  = new Context();
    private static Logger logger = LoggerFactory.getLogger(ThymeleafHandler.class);
    private static ThymeleafHandler thymeleafHandler = new ThymeleafHandler();
    Gson gson = new Gson();
    static {
        templateEngine.getConfiguration();
    }

    private ThymeleafHandler (){
    }
    public static ThymeleafHandler getInstance(){
        return thymeleafHandler;
    }

    /**
     * 模板字符串
     * @param htmlStr
     * @return
     */
    public String templateEngineProcessByString(String htmlStr, JSONObject var){
        if(var != null){
           String jsonStr = var.toJSONString().replace("<","&lt;").replace(">","&gt;");
            context.setVariables(gson.fromJson(jsonStr, Map.class));
        }
        String r = null;
        try{
            r = templateEngine.process(htmlStr, context);
        }catch(Exception e){
            r = e.getCause().toString().replace("org.attoparser.ParseException: Exception evaluating OGNL expression:","The Data paraeter error:");
            logger.debug(r);
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
     * @return
     */
    public String templateEngineProcessByPath(String filePath, JSONObject var) throws IOException {
        //HashMap map = (HashMap)JSON.parseObject(var.toJSONString(),Map.class);
        if(var != null){
            context.setVariables(JSON.parseObject(var.toJSONString(),Map.class));
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
