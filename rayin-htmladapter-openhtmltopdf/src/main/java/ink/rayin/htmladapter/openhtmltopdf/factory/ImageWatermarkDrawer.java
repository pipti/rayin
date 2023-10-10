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

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

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
        PdfBoxOutputDevice pdfBoxOutputDevice = (PdfBoxOutputDevice) outputDevice;
        float pageHeight = pdfBoxOutputDevice.getPage().getMediaBox().getHeight();
        float pageWidth = pdfBoxOutputDevice.getPage().getMediaBox().getWidth();

        String opacity = null;
        try {
            opacity = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"),"opacity");
            String degStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"),"transform");
            String src = e.getAttribute("src");
            String imgWidthStr =  CSSParser.getSingleStylePropertyValue(e.getAttribute("style"),"width");
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

           // String os = System.getProperty("os.name");
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
                return null;
            }

            //log.debug("imgBos.size()"+imgBos.size());
//            if(imgBos.size()/1024 > 30){
//                log.warn("水印图片有点大噢！");
//            }
            if(StringUtil.isBlank(opacity)){
                opacity = "0.5";
            }
            PDDocument pdd = ((PdfBoxOutputDevice) outputDevice).getWriter();
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(pdd, imgBos.toByteArray(), "");
            PDExtendedGraphicsState pdfExtState = new PDExtendedGraphicsState();
            float imgHeight = (imgWidth/pdImage.getWidth())*pdImage.getHeight();

            // 设置透明度
            pdfExtState.setNonStrokingAlphaConstant(Float.parseFloat(opacity));
            pdfExtState.setAlphaSourceFlag(true);
            pdfExtState.getCOSObject().setItem(COSName.MASK, COSName.MULTIPLY);

            int deg = 0;
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

            // 旋转后的矩形宽高
            // width = w*cosα + h*sinα
            // height = h*cosα + w*sinα
            float rotateWidth = imgWidth * (float)Math.cos(Math.toRadians(deg)) + imgHeight * (float)Math.sin(Math.toRadians(deg));
            float rotateHeight = imgHeight * (float)Math.cos(Math.toRadians(deg)) + imgWidth * (float)Math.sin(Math.toRadians(deg));

            PDPage page;
            PDPageContentStream contentStream;

            int pageCount = pdd.getNumberOfPages();

            for(int p = 0; p < pageCount; p++){
                page = pdd.getPage(p);

                contentStream = new PDPageContentStream(pdd, page, PDPageContentStream.AppendMode.APPEND, true, true);
                contentStream.setGraphicsStateParameters(pdfExtState);

                // 根据纸张大小添加水印
                for (int h = 0; h < pageHeight; h = h + (int)rotateHeight + 20) {
                    for (int w = 0; w < pageWidth; w = w + (int)rotateWidth + 20) {
                        try {
                            Matrix matrix = new Matrix();
                            // 位置
                            matrix.translate(w, h);
                            // 旋转角度
                            matrix.rotate(Math.toRadians(deg));
                            // 修正图片大小
                            matrix.scale(imgWidth, imgHeight);
                            // 绘制
                            contentStream.drawImage(pdImage, matrix);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                }

                // 结束渲染，关闭流
                contentStream.restoreGraphicsState();
                contentStream.close();

            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }






//        outputDevice.drawWithGraphics(0 , 0, (float) pageWidth*2,
//                (float) pageHeight*2, (Graphics2D g2d) -> {
//
//                    double realWidth = width / dotsPerPixel;
//                    double realHeight = height / dotsPerPixel;
//                    float fontSize = 25f;
//                    Font font;
//                    Color colorRGB = Color.RED;
//                    try {
//                        String fontStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"),"font-family");
//                        if(StringUtil.isBlank(fontStr)){
//                            fontStr = "HKTSongW9";
//                        }
//                        String colorStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"),"color");
//                        if(StringUtil.isBlank(colorStr)){
//                            colorStr = "red";
//                        }
//                        if(colorStr.toLowerCase().indexOf("rgb") < 0 && colorStr.toLowerCase().indexOf("#") < 0){
//                            Field field = Class.forName("java.awt.Color").getField(colorStr);
//                            colorRGB =  (Color)field.get(null);
//                        }else{
//                            if(colorStr.toLowerCase().indexOf("#") >= 0){
//                                colorRGB = Color.getColor(colorStr);
//                            }else if(colorStr.toLowerCase().indexOf("rgb") >= 0){
//                                String[] rgb = colorStr.substring(4,colorStr.length() - 1).split(",");
////
//                                //int color = (int)Long.parseLong(colorStr, 16);
//                                int r = Integer.parseInt(rgb[0].trim());
//                                int g = Integer.parseInt(rgb[1].trim());
//                                int b = Integer.parseInt(rgb[2].trim());
//                                colorRGB = new Color(r,g,b);
//                            }
//                        }
////                        if(StringUtil.isNotBlank(colorStr)){
////                            String[] rgb = colorStr.substring(4,colorStr.length() - 1).split(",");
////
////                            //int color = (int)Long.parseLong(colorStr, 16);
////                            int r = Integer.parseInt(rgb[0].trim());
////                            int g = Integer.parseInt(rgb[1].trim());
////                            int b = Integer.parseInt(rgb[2].trim());
////                            colorRGB = new Color(r,g,b);
////                        }
//
//
//                        // TODO
//                        if(StringUtil.isNotBlank(fontSizeStr)){
//                            if(fontSizeStr.indexOf("px") > 0){
//                                fontSizeStr = fontSizeStr.replace("px","");
//                                fontSize = Float.parseFloat(fontSizeStr)/dotsPerPixel;
//                            }
//                            if(fontSizeStr.indexOf("pt") > 0){
//                                fontSizeStr = fontSizeStr.replace("pt","");
//                                fontSize = Float.parseFloat(fontSizeStr);
//                            }
//                            fontSize = Float.parseFloat(fontSizeStr);
//                        }
//                        Font parent = Font.createFont(Font.TRUETYPE_FONT, fontCache.get(fontStr).supply());
//
//                        //Font parent = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/eric/Documents/dev_projects/opensource/rayin/rayin-htmladapter-base/src/main/resources/rayin_default_fonts/FangSong.ttf"));
//                        font = parent.deriveFont(fontSize);
//                    } catch (FontFormatException | IOException e1) {
//                        e1.printStackTrace();
//                        throw new RuntimeException(e1);
//                    } catch (NoSuchFieldException ex) {
//                        throw new RuntimeException(ex);
//                    } catch (ClassNotFoundException ex) {
//                        throw new RuntimeException(ex);
//                    } catch (IllegalAccessException ex) {
//                        throw new RuntimeException(ex);
//                    }
//
//                    Rectangle2D bounds = font.getStringBounds(e.getAttribute("value"), g2d.getFontRenderContext());
//
//                    String opacityStr = null;
//                    try {
//                        opacityStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"),"opacity");
//                    } catch (IOException ex) {
//                        throw new RuntimeException(ex);
//                    }
//                    float opacity = 0.3f;
//                    if(StringUtil.isNumeric(opacityStr)){
//                        opacity = Float.parseFloat(opacityStr);
//                    }
//
//                    g2d.setFont(font);
//                    g2d.setPaint(colorRGB);
//                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
//                    int colCount = new Double(pageWidth/bounds.getWidth()*2).intValue();
//                    int rowCount = new Double(pageHeight/bounds.getHeight()*2).intValue();
//
//                        for(int i = 0; i< rowCount; i++){
//                            for(int j = 0; j< colCount; j++){
//                                g2d.drawString(e.getAttribute("value"),
//                                        (float) (j * bounds.getWidth() * 2),
//                                        (float) (i * bounds.getHeight() * 3));
//                            }
//                         }
//
//
//                });

        return null;
    }
}
