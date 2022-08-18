package ink.rayin.htmladapter.base.utils;

import com.steadystate.css.parser.CSSOMParser;
import lombok.extern.slf4j.Slf4j;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

@Slf4j
public class CSSParser {

    public static boolean checkCssProperty(String cssStr,String selectorStr,String property){
        try {
            InputSource source = new InputSource(cssStr);
            //source.(inStream);
            source.setEncoding("UTF-8");
            final CSSOMParser parser = new CSSOMParser();
            CSSStyleSheet sheet = parser.parseStyleSheet(source, null, null);
            CSSRuleList rules = sheet.getCssRules();
            if(rules.getLength() == 0 ){
                return false;
            }
            for (int i = 0; i < rules.getLength(); i++) {
                final CSSRule rule = rules.item(i);
                //获取选择器名称
                String selectorText_ = ((CSSStyleRule) rule).getSelectorText();
                //获取样式内容
                String cssText = ((CSSStyleRule)rule).getCssText();

                if(selectorStr.equals(selectorText_)){
                    CSSStyleDeclaration ss =  ((CSSStyleRule)rule).getStyle();
                    String propertyValue = ss.getPropertyValue(property);
                    if("".equals(propertyValue) || propertyValue == null){
                        return false;
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            log.error("checkCssProperty***************************************"+e.getMessage());
            return false;
        }
        return false;
    }

    public static CSSStyleSheet addRuleProperty(String cssStr,String selectorText,String propertyName,String propertyValue,String priority){
        CSSStyleSheet sheet = null;
        try {
            InputSource source = new InputSource(cssStr);
            //source.setByteStream(inStream);
            source.setEncoding("UTF-8");
            final CSSOMParser parser = new CSSOMParser();
            sheet = parser.parseStyleSheet(source, null, null);
            CSSRuleList rules = sheet.getCssRules();
            if(rules.getLength() == 0 ){
                return null;
            }
            for (int i = 0; i < rules.getLength(); i++) {
                final CSSRule rule = rules.item(i);
                //获取选择器名称
                String selectorText_ = ((CSSStyleRule) rule).getSelectorText();
                //获取样式内容
                String cssText = ((CSSStyleRule)rule).getCssText();

                if(selectorText.equals(selectorText_)){
                    CSSStyleDeclaration cd =  ((CSSStyleRule)rule).getStyle();
                    cd.setProperty(propertyName, propertyValue, priority);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            log.error("addRuleProperty***************************************"+e.getMessage());
            return null;
        }
        return sheet;
    }


    public static void main(String[] args){
        String q = "body {\n" +
                "            font-family: FangSong,HanaMinB;\n" +
                "            line-height: 1.2;\n" +
                "            font-size:12px;\n" +
                "            font-weight:normal\n" +
                "            /*设置背景色*/\n" +
                "            /*background: #00FF00 ;*/\n" +
                "            /*设置背景图片*/\n" +
                "            /*background-image:url(data:image/gif;base64,AAAA) no-repeat fixed top;*/\n" +
                "        }";

        //log.debug(CSSParser.checkCssProperty(q, "body", "backage"));
    }
}
