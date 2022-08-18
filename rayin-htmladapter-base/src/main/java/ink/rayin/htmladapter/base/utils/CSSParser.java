package ink.rayin.htmladapter.base.utils;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;
import ink.rayin.tools.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.*;

import java.io.IOException;
import java.io.StringReader;

@Slf4j
public class CSSParser {

    /**
     * 检查css属性是否存在
     * @param cssStr css字符串
     * @param selectorStr css选择器字符串
     * @param property css属性字符串
     * @return
     * @throws IOException
     */
    public static boolean checkCssProperty(String cssStr, String selectorStr, String property) throws IOException {
        InputSource source = new InputSource(new StringReader(cssStr));
        source.setEncoding("UTF-8");
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
        CSSRuleList rules = sheet.getCssRules();
        if(rules.getLength() == 0 ){
            return false;
        }
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);
            //获取选择器名称
            String selectorText_ = ((CSSStyleRule) rule).getSelectorText();
            if(selectorStr.equals(selectorText_)){
                CSSStyleDeclaration ss =  ((CSSStyleRule)rule).getStyle();
                String propertyValue = ss.getPropertyValue(property);
                if("".equals(propertyValue) || propertyValue == null){
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public static boolean checkCssPropertyAndValue(String cssStr, String selectorStr, String property, String value) throws IOException {
        InputSource source = new InputSource(new StringReader(cssStr));
        source.setEncoding("UTF-8");
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
        CSSRuleList rules = sheet.getCssRules();
        if(rules.getLength() == 0 ){
            return false;
        }
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);
            //获取选择器名称
            String selectorText_ = "";
            if(rule instanceof CSSPageRule){
                selectorText_ = ((CSSPageRule) rule).getSelectorText();
                if(selectorStr.equals(selectorText_)){
                    CSSStyleDeclaration ss =  ((CSSPageRule)rule).getStyle();
                    String propertyValue = ss.getPropertyValue(property);
                    if("".equals(propertyValue) || propertyValue == null || !propertyValue.equals(value)){
                        return false;
                    }
                    return true;
                }
            }
            if(rule instanceof CSSStyleRule){
                selectorText_ = ((CSSStyleRule) rule).getSelectorText();
                if(selectorStr.equals(selectorText_)){
                    CSSStyleDeclaration ss =  ((CSSStyleRule)rule).getStyle();
                    String propertyValue = ss.getPropertyValue(property);
                    if("".equals(propertyValue) || propertyValue == null || !propertyValue.equals(value)){
                        return false;
                    }
                    return true;
                }
            }


        }
        return false;
    }

    public static boolean checkSingleStylePropertyAndValue(String cssStr, String property, String value) throws IOException {
        InputSource source = new InputSource(new StringReader(cssStr));
        source.setEncoding("UTF-8");
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        CSSStyleDeclaration decl = parser.parseStyleDeclaration(source);
        if(StringUtil.isBlank(decl.getPropertyValue(property)) || !decl.getPropertyValue(property).equals(value)){
            return false;
        }
        return true;
    }

    /**
     * 添加css属性
     * @param cssStr css字符串
     * @param selectorText css选择器字符串
     * @param propertyName 属性名称
     * @param propertyValue 属性值
     * @param priority 优先级 ，可空
     * @return
     * @throws IOException
     */
    public static CSSStyleSheet addRuleProperty(String cssStr,
                                                String selectorText,
                                                String propertyName,
                                                String propertyValue,
                                                String priority) throws IOException {
        InputSource source = new InputSource(new StringReader(cssStr));
        source.setEncoding("UTF-8");
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
        CSSRuleList rules = sheet.getCssRules();
        if(rules.getLength() == 0 ){
            return null;
        }
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);
            //获取选择器名称
            String selectorText_ = ((CSSStyleRule) rule).getSelectorText();

            if(selectorText.equals(selectorText_)){
                CSSStyleDeclaration cd =  ((CSSStyleRule)rule).getStyle();
                cd.setProperty(propertyName, propertyValue, priority);
            }
        }
        return sheet;
    }

    public static CSSStyleSheet addRuleProperty(CSSStyleSheet sheet,
                                                String selectorText,
                                                String propertyName,
                                                String propertyValue,
                                                String priority) {
        CSSRuleList rules = sheet.getCssRules();
        if(rules.getLength() == 0 ){
            return null;
        }
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);
            //获取选择器名称
            String selectorText_ = ((CSSStyleRule) rule).getSelectorText();

            if(selectorText.equals(selectorText_)){
                CSSStyleDeclaration cd =  ((CSSStyleRule)rule).getStyle();
                cd.setProperty(propertyName, propertyValue, priority);
            }
        }
        return sheet;
    }

    /**
     * 根据css选择器删除css样式
     * @param cssStr css字符串
     * @param selectorText css选择器
     * @return
     * @throws IOException
     */
    public static CSSStyleSheet deleteRule(String cssStr, String selectorText) throws IOException {
        InputSource source = new InputSource(new StringReader(cssStr));
        source.setEncoding("UTF-8");
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
        CSSRuleList rules = sheet.getCssRules();
        if(rules.getLength() == 0 ){
            return null;
        }
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);
            //获取选择器名称
            String selectorText_ = ((CSSStyleRule) rule).getSelectorText();
            if(selectorText.equals(selectorText_)){
                sheet.deleteRule(i);
            }
        }

        return sheet;
    }

    public static CSSStyleSheet deleteRule(CSSStyleSheet sheet, String selectorText) {
        CSSRuleList rules = sheet.getCssRules();
        if(rules.getLength() == 0 ){
            return null;
        }
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);
            //获取选择器名称
            String selectorText_ = ((CSSStyleRule) rule).getSelectorText();
            if(selectorText.equals(selectorText_)){
                sheet.deleteRule(i);
            }
        }

        return sheet;
    }

    /**
     * 删除css属性
     * @param cssStr css字符串
     * @param selectorText css选择器
     * @param propertyName css属性名称
     * @return
     * @throws IOException
     */
    public static CSSStyleSheet deleteRuleProperty(String cssStr, String selectorText, String propertyName) throws IOException {
        InputSource source = new InputSource(new StringReader(cssStr));
        source.setEncoding("UTF-8");
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
        CSSRuleList rules = sheet.getCssRules();
        if(rules.getLength() == 0 ){
            return null;
        }
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);
            //获取选择器名称
            String selectorText_ = ((CSSStyleRule) rule).getSelectorText();
            if(selectorText.equals(selectorText_)){
                CSSStyleDeclaration cd =  ((CSSStyleRule)rule).getStyle();
                cd.removeProperty(propertyName);
            }
        }

        return sheet;
    }

    public static CSSStyleDeclaration deleteSingleStyleProperty(String cssStr, String propertyName) throws IOException {
        InputSource source = new InputSource(new StringReader(cssStr));
        source.setEncoding("UTF-8");
        final CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        CSSStyleDeclaration decl = parser.parseStyleDeclaration(source);
        decl.removeProperty(propertyName);
        return decl;
    }

    public static CSSStyleDeclaration deleteSingleStyleProperty(CSSStyleDeclaration decl, String propertyName) {
        decl.removeProperty(propertyName);
        return decl;
    }

    public static CSSStyleSheet deleteRuleProperty(CSSStyleSheet sheet, String selectorText, String propertyName) {
        CSSRuleList rules = sheet.getCssRules();
        if(rules.getLength() == 0 ){
            return null;
        }
        for (int i = 0; i < rules.getLength(); i++) {
            final CSSRule rule = rules.item(i);
            //获取选择器名称
            String selectorText_ = ((CSSStyleRule) rule).getSelectorText();
            if(selectorText.equals(selectorText_)){
                CSSStyleDeclaration cd =  ((CSSStyleRule)rule).getStyle();
                cd.removeProperty(propertyName);
            }
        }

        return sheet;
    }

    public static void main(String[] args) throws IOException {
        String q = "body {\n" +
                "            font-family: FangSong,HanaMinB;\n" +
                "            line-height: 1.2;\n" +
                "            font-size:12px;\n" +
                "            font-weight:normal\n" +
                "            /*设置背景色*/\n" +
                "            /*background: #00FF00 ;*/\n" +
                "            /*设置背景图片*/\n" +
                "            background-image:url(data:image/gif;base64,AAAA) no-repeat fixed top;\n" +
                "        }";
        String a = "background: #ffcc44";
        String b = "body {/*background: #00FF00 ;*/font-family: FangSong,HanaMinB;background-image:url(data:image/gif;base64,AAAA) no-repeat fixed top;background: #00FF00 ;-fs-print-hidden:true}";
        log.debug(CSSParser.checkCssProperty(b, "body", "background") + "");
        log.debug(CSSParser.deleteRuleProperty(b, "body", "background-image").toString());
        log.debug(CSSParser.checkCssPropertyAndValue(b, "body", "-fs-print-hidden", "true") + "");

        log.debug(CSSParser.checkCssProperty(a, "", "background") + "");
    }
}
