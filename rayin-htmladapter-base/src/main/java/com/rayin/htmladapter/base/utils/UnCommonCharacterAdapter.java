package com.rayin.htmladapter.base.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rayin.tools.utils.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTML转PDF前的生僻字处理
 * 描述:
 * PDF不会自动适应嵌入至其中的字体，通用的字库无法显示生僻字，如果需要显示必须指定生僻字的生僻字字库
 * 1、一种方式是通过自造字库，将生僻字纳入字库中，所以的字体指定该字库，此种方式不够灵活，限制了字体的使用
 * 2、另外一个方式在生成PDF前，在HTML指定相应的字体，覆盖默认的字体
 * 采用第二种方式，通过配置文件配置生僻字对应的字体，通过正则表达式检索需要设置字体的生僻字，将生僻字强制通过标签进行字体设置。
 *
 * @author WangZhu
 * @Date 2019-12-08 18:10:31
 * @version 1.00
 * @since 1.8
 */
@Deprecated
public class UnCommonCharacterAdapter{
    private static Logger logger = LoggerFactory.getLogger(UnCommonCharacterAdapter.class);
    private final static String unCommonChars = "uncommonchars.json";
    private final static String unCommonCharsPrj = "uncommonchars-prj.json";
    private static JsonNode jsonNodeAll ;

    static{
        synchronized(UnCommonCharacterAdapter.class) {

            try {
                jsonNodeAll = JsonSchemaValidator.getJsonNodeFromInputStream(ResourceUtil.getResourceAsStream(unCommonChars));
                JsonNode jsonNode2 = JsonSchemaValidator.getJsonNodeFromInputStream(ResourceUtil.getResourceAsStream(unCommonCharsPrj));

                if(jsonNode2 != null && jsonNode2.size() > 0) {
                    String all = (jsonNodeAll.toString() + jsonNode2.toString()).replace("}{",",").replace("][",",")
                            .replace("[,","[");
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonNodeAll = objectMapper.readTree(all);
                }

            } catch (IOException e) {
                logger.error("生僻字未加载，没找到配置文件" + e.getMessage());
            }
        }
    }

    private UnCommonCharacterAdapter() {}

    public static void unCommonCharsReload(){
        synchronized(UnCommonCharacterAdapter.class) {

            try {
                jsonNodeAll = JsonSchemaValidator.getJsonNodeFromInputStream(ResourceUtil.getResourceAsStream(unCommonChars));
                JsonNode jsonNode2 = JsonSchemaValidator.getJsonNodeFromInputStream(ResourceUtil.getResourceAsStream(unCommonCharsPrj));

                if(jsonNode2 != null && jsonNode2.size() > 0) {
                    String all = (jsonNodeAll.toString() + jsonNode2.toString()).replace("}{",",").replace("][",",")
                            .replace("[,","[");
                    ObjectMapper objectMapper = new ObjectMapper();
                    jsonNodeAll = objectMapper.readTree(all);
                }

            } catch (IOException e) {
                logger.error("生僻字配置未加载，没找到相关配置文件",e);
            }
        }
    }

    /**
     * 替换生僻字，由于pdf不能动态兼容字库显示字体因此需要对生僻字指定显示的字库
     * @param html
     * @return
     * @throws IOException
     */
    public static String UnCommonCharacter(String html) throws IOException {
        Iterator<JsonNode> elements = null;
        if(jsonNodeAll != null){
            elements = jsonNodeAll.elements();
        }else{
            logger.warn("未找到生僻词的配置！");
            return html;
        }
        String regEx;
        Pattern p;

        while(elements.hasNext()) {
            JsonNode node = elements.next();
            String fontFamily = node.get("fontFamily").asText();
            regEx = "[" + node.get("chars").asText() +"]";
            Pattern pattern = Pattern.compile(regEx);
            Matcher m = pattern.matcher(html);
            HashSet<String> findStr = new HashSet<String>();
            while(m.find()) {
                findStr.add(m.group());
            }

            Iterator<String> it = findStr.iterator();
            while(it.hasNext()) {
                String tmp = it.next();
                html = html.replace(tmp,"<span style=\"font-family: "+ fontFamily + ";\">" + tmp + "</span>");
            }

        }
        return html;
    }



    public static void main(String[] args){
        String k = "䶮\uD840\uDDD4·\uD87A\uDDF5㵘兀㽏";
        String a = "䶮\uD840\uDDD4·\uD87A\uDDF5㵘兀㽏";
        String b = "<font>你好帅哈哈䶮</font>";
        Set<Character> seta = new HashSet<Character>();
        Set<Character> setb = new HashSet<Character>();

        //把a中字母分别存入集合seta
        for(int i=0;i<a.length();i++){
            seta.add(a.charAt(i));
        }

        //把b中字母分别存入集合setb
        for(int j=0;j<b.length();j++){
            setb.add(b.charAt(j));
        }

        //返回公共元素
        seta.retainAll(setb);
        for(Character m:seta){
            b = b.replace(m.toString(),"<label>" + m.toString() + "</label>");
        }
        System.out.println(seta);
        System.out.println(b);
    }
}
