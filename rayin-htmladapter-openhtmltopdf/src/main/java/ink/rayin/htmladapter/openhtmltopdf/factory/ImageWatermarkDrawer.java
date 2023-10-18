package ink.rayin.htmladapter.openhtmltopdf.factory;

import java.awt.*;
import java.io.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.openhtmltopdf.extend.FSObjectDrawer;
import com.openhtmltopdf.extend.OutputDevice;
import com.openhtmltopdf.pdfboxout.PdfBoxOutputDevice;
import com.openhtmltopdf.render.RenderingContext;

import ink.rayin.htmladapter.openhtmltopdf.utils.PdfBoxTools;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.w3c.dom.Element;
import lombok.extern.slf4j.Slf4j;

import ink.rayin.htmladapter.base.utils.CSSParser;
import ink.rayin.tools.utils.*;

/**
 * 图片水印插件
 *
 * @author Jonah Wang <br>
 * html标签样例，如果会生成多页需要将标签放到&lt;body&gt;...最后位置&lt;/body&gt; <br>
 * &lt;object type="img/watermark" src="images/rayin.png" style="opacity: 0.5;transform:rotate(30deg);width:100px"/&gt;
 *
 */
@Slf4j
public class ImageWatermarkDrawer implements FSObjectDrawer {
    public ImageWatermarkDrawer() {
    }

    @Override
    public Map<Shape, String> drawObject(Element e, double x, double y, final double width, final double height, OutputDevice outputDevice, RenderingContext ctx, final int dotsPerPixel)  {

        if(ctx.getPageCount() != ctx.getPageNo() + 1){
            return null;
        }
        PDDocument pdd = ((PdfBoxOutputDevice) outputDevice).getWriter();

        try {
            if(StringUtil.isBlank(e.getAttribute("value"))){
                log.error("value", "value is null");
                return null;
            }
            ImageWatermarkDrawer.setWatermark(pdd, e.getAttribute("value"), e.getAttribute("style"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    public static void setWatermark(PDDocument pdd, String src, String style) throws IOException {
        ByteArrayOutputStream imgBos = new ByteArrayOutputStream();
        if(StringUtil.isNotBlank(src)){
            if(src.startsWith("data:image/")){
                String base64Str = src.replaceFirst("data:.*;base64,", "");
                byte[] imgByte = Base64Util.decodeFromString(base64Str);
                IoUtil.copy(imgByte, imgBos);
            }else if (src.startsWith("http://") || src.startsWith("https://") || src.startsWith("file:")) {
                if(src.startsWith("file:")){
                    src = src.replace("file:", "");
                    src = src.replace("\\", "/");
                }
                imgBos = ResourceUtil.getResourceAsByte(src);
            }else if (src.startsWith("/") || src.startsWith("\\")) {
                //  src = "file:" + "//" + src;
                src = src.replace("\\" , "/");
                log.debug("image url convert:" + src);
                imgBos = ResourceUtil.getResourceAsByte(src);
            }else{
                src = src.replace("\\" , "/");
                imgBos = ResourceUtil.getResourceAsByte(src);
            }
        }else{
            throw new RuntimeException("src is null");
        }

        log.debug("imgBos.size()"+imgBos.size());
        if(imgBos.size()/1024 > 30){
            log.warn("水印图片有点大噢！");
        }
        float opacity = 0.5f;
        String opacityStr = CSSParser.getSingleStylePropertyValue(style,"opacity");
        String degStr = CSSParser.getSingleStylePropertyValue(style,"transform");
        String marginStr = CSSParser.getSingleStylePropertyValue(style,"margin");
        String imgWidthStr =  CSSParser.getSingleStylePropertyValue(style,"width");
        float imgWidth = 100;
        if(StringUtil.isNotBlank(imgWidthStr)){
            if(imgWidthStr.indexOf("px") > 0){
                imgWidthStr = imgWidthStr.replace("px","");
                imgWidth = Float.parseFloat(imgWidthStr) * 0.75f;
            }
            if(imgWidthStr.indexOf("pt") > 0){
                imgWidthStr = imgWidthStr.replace("pt","");
                imgWidth = Float.parseFloat(imgWidthStr);
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
        int deg = 40;
        if(StringUtil.isNotBlank(degStr)){
            String regex = "(?<=[rotate(])\\d+(?=[deg)])";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(degStr);

            while (matcher.find()) {
                String number = matcher.group();
                deg = Integer.parseInt(number);
                break;
            }
        }

        if(StringUtil.isNotBlank(opacityStr)){
            opacity = Float.parseFloat(opacityStr);
        }

        PdfBoxTools.setImageWatermark(pdd, imgBos,imgWidth, deg, opacity, margin);
    }

}
