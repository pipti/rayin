package ink.rayin.htmladapter.openhtmltopdf.factory;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.openhtmltopdf.extend.FSObjectDrawer;
import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.extend.OutputDevice;
import com.openhtmltopdf.pdfboxout.PdfBoxOutputDevice;
import com.openhtmltopdf.render.RenderingContext;

import ink.rayin.htmladapter.openhtmltopdf.utils.PdfBoxTools;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.w3c.dom.Element;
import lombok.extern.slf4j.Slf4j;

import ink.rayin.htmladapter.base.utils.CSSParser;
import ink.rayin.tools.utils.StringUtil;

/**
 * 文字水印插件
 *
 * @author Jonah Wang <br>
 * html标签样例，如果会生成多页需要将标签放到&lt;body&gt;...最后位置&lt;/body&gt; <br>
 * &lt;object type="font/watermark" value="文字水印" style="opacity: 0.5;transform:rotate(30deg);width:100px"/&gt;
 *
 */
@Slf4j
public class FontWatermarkDrawer implements FSObjectDrawer {
    HashMap<String, FSSupplier<InputStream>> fontCache;

    public FontWatermarkDrawer(HashMap<String, FSSupplier<InputStream>> fontCache){
        this.fontCache = fontCache;
    }

    public FontWatermarkDrawer() {
    }

    @Override
    public Map<Shape, String> drawObject(Element e, double x, double y, double width, double height,
                                         OutputDevice outputDevice, RenderingContext ctx, int dotsPerPixel) {
        if(ctx.getPageCount() != ctx.getPageNo() + 1){
            return null;
        }
        PDDocument pdd = ((PdfBoxOutputDevice) outputDevice).getWriter();
        String fontStr = null;
        try {
            fontStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"), "font-family");
            if (StringUtil.isBlank(fontStr)) {
                fontStr = "DFPSongW7";
            }

            setWatermark(pdd, e.getAttribute("value"), e.getAttribute("style"), fontCache.get(fontStr).supply());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (FontFormatException ex) {
            throw new RuntimeException(ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        return null;
    }
    public static void setWatermark(PDDocument pdd, String value, String style,  InputStream fontIs) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException, FontFormatException {
        String opacityStr = CSSParser.getSingleStylePropertyValue(style, "opacity");
        String colorStr = CSSParser.getSingleStylePropertyValue(style, "color");
        String degStr = CSSParser.getSingleStylePropertyValue(style, "transform");
        String marginStr = CSSParser.getSingleStylePropertyValue(style,"margin");
        // 设置字体和字号
        String fontSizeStr = CSSParser.getSingleStylePropertyValue(style, "font-size");
        float fontSize = 20;
        // TODO
        if (StringUtil.isNotBlank(fontSizeStr)) {
            if (fontSizeStr.indexOf("px") > 0) {
                fontSizeStr = fontSizeStr.replace("px", "");
                fontSize = Float.parseFloat(fontSizeStr) * 0.75f;
            }
            if (fontSizeStr.indexOf("pt") > 0) {
                fontSizeStr = fontSizeStr.replace("pt", "");
                fontSize = Float.parseFloat(fontSizeStr);
            }
        }

        float opacity = 0.5f;
        if (StringUtil.isNotBlank(opacityStr)) {
            opacity = Float.parseFloat(opacityStr);
        }

        if (StringUtil.isBlank(value)) {
            value = "未设置value";
        }
        int deg = 30;
        if (StringUtil.isNotBlank(degStr)) {
            String regex = "(?<=[rotate(])\\d+(?=[deg)])";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(degStr);

            while (matcher.find()) {
                String number = matcher.group();
                deg = Integer.parseInt(number);
                break;
            }
        }
        if (StringUtil.isBlank(colorStr)) {
            colorStr = "red";
        }
        Color colorRGB = Color.RED;
        if (colorStr.toLowerCase().indexOf("rgb") < 0 && colorStr.toLowerCase().indexOf("#") < 0) {
            Field field = Class.forName("java.awt.Color").getField(colorStr);
            colorRGB = (Color) field.get(null);
        } else {
            if (colorStr.toLowerCase().indexOf("#") >= 0) {
                colorRGB = Color.getColor(colorStr);
            } else if (colorStr.toLowerCase().indexOf("rgb") >= 0) {
                String[] rgb = colorStr.substring(4, colorStr.length() - 1).split(",");
                int r = Integer.parseInt(rgb[0].trim());
                int g = Integer.parseInt(rgb[1].trim());
                int b = Integer.parseInt(rgb[2].trim());
                colorRGB = new Color(r, g, b);
            } else {
                Class objectName = Class.forName("java.awt.Color." + colorStr.toUpperCase());
                colorRGB = (Color) objectName.newInstance();
            }
        }
        float margin = 30;
        if(StringUtil.isNotBlank(marginStr)){
            if(marginStr.indexOf("px") > 0){
                marginStr = marginStr.replace("px","");
                margin = Float.parseFloat(marginStr) * 0.75f;
            }
            if(marginStr.indexOf("pt") > 0){
                marginStr = marginStr.replace("pt","");
                margin = Float.parseFloat(marginStr);
            }
        }
        PdfBoxTools.setFontWatermark(pdd, value, deg, opacity, colorRGB, fontIs, fontSize, margin);
    }

}
