package ink.rayin.htmladapter.openhtmltopdf.factory;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
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

import de.rototor.pdfbox.graphics2d.PdfBoxGraphics2D;
import ink.rayin.tools.utils.IoUtil;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

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
        PdfBoxOutputDevice pdfBoxOutputDevice = (PdfBoxOutputDevice) outputDevice;
        float pageHeight = pdfBoxOutputDevice.getPage().getMediaBox().getHeight();
        float pageWidth = pdfBoxOutputDevice.getPage().getMediaBox().getWidth();

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


//
//        outputDevice.drawWithGraphics(0 , 0, (float) pageWidth*2,
//                    (float) pageHeight*2, (Graphics2D g2d) -> {
//                try {
//
//                    String fontStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"), "font-family");
//                    String opacity = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"), "opacity");
//                    String colorStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"), "color");
//                    String degStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"), "transform");
//
//                    // 设置字体和字号
//                    String fontSizeStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"), "font-size");
//                    float fontSize = 20;
//                    // TODO
//                    if (StringUtil.isNotBlank(fontSizeStr)) {
//                        if (fontSizeStr.indexOf("px") > 0) {
//                            fontSizeStr = fontSizeStr.replace("px", "");
//                            fontSize = Float.parseFloat(fontSizeStr) * 0.75f;
//                        }
//                        if (fontSizeStr.indexOf("pt") > 0) {
//                            fontSizeStr = fontSizeStr.replace("pt", "");
//                            fontSize = Float.parseFloat(fontSizeStr);
//                        }
//                    }
//                    if (StringUtil.isBlank(fontStr)) {
//                        fontStr = "DFPSongW7";
//                    }
//                    if (StringUtil.isBlank(opacity)) {
//                        opacity = "0.5";
//                    }
//                    String value = e.getAttribute("value");
//                    if (StringUtil.isBlank(value)) {
//                        value = "未设置value";
//                    }
//
//                    PDFont font = PDType0Font.load(pdd, fontCache.get(fontStr).supply(), true);
//                    PDExtendedGraphicsState pdfExtState = new PDExtendedGraphicsState();
//
//                    // 设置透明度
//                    pdfExtState.setNonStrokingAlphaConstant(Float.parseFloat(opacity));
//                    pdfExtState.setAlphaSourceFlag(true);
//                    pdfExtState.getCOSObject().setItem(COSName.MASK, COSName.MULTIPLY);
//
//                    Font font2d;
//                    Font parent = Font.createFont(Font.TRUETYPE_FONT, fontCache.get(fontStr).supply());
//                    font2d = parent.deriveFont(fontSize);
//
//                    Rectangle2D bounds = font2d.getStringBounds(value, g2d.getFontRenderContext());
//                    float fontWidth = (float) bounds.getWidth();
//                    float fontHeight = (float) bounds.getHeight();
//                    int deg = 30;
//                    if (StringUtil.isNotBlank(degStr)) {
//                        String regex = "(?<=[rotate(])\\d+(?=[deg)])";
//                        Pattern pattern = Pattern.compile(regex);
//                        Matcher matcher = pattern.matcher(degStr);
//
//                        while (matcher.find()) {
//                            String number = matcher.group();
//                            deg = Integer.parseInt(number);
//                            break;
//                        }
//                    }
//                    if (StringUtil.isBlank(colorStr)) {
//                        colorStr = "red";
//                    }
//                    Color colorRGB = Color.RED;
//                    if (colorStr.toLowerCase().indexOf("rgb") < 0 && colorStr.toLowerCase().indexOf("#") < 0) {
//                        Field field = Class.forName("java.awt.Color").getField(colorStr);
//                        colorRGB = (Color) field.get(null);
//                    } else {
//                        if (colorStr.toLowerCase().indexOf("#") >= 0) {
//                            colorRGB = Color.getColor(colorStr);
//                        } else if (colorStr.toLowerCase().indexOf("rgb") >= 0) {
//                            String[] rgb = colorStr.substring(4, colorStr.length() - 1).split(",");
//                            //int color = (int)Long.parseLong(colorStr, 16);
//                            int r = Integer.parseInt(rgb[0].trim());
//                            int g = Integer.parseInt(rgb[1].trim());
//                            int b = Integer.parseInt(rgb[2].trim());
//                            colorRGB = new Color(r, g, b);
//                        } else {
//                            Class objectName = Class.forName("java.awt.Color." + colorStr.toUpperCase());
//                            colorRGB = (Color) objectName.newInstance();
//                        }
//                    }
//                    // 旋转后的矩形宽高
//                    // width = w*cosα + h*sinα
//                    // height = h*cosα + w*sinα
//                    float rotateWidth = fontWidth * (float) Math.cos(Math.toRadians(deg)) + fontHeight * (float) Math.sin(Math.toRadians(deg));
//                    float rotateHeight = fontHeight * (float) Math.cos(Math.toRadians(deg)) + fontWidth * (float) Math.sin(Math.toRadians(deg));
//
//                    int pageCount = pdd.getNumberOfPages();
//                    PDPage page;
//
//                    for (int p = 0; p < pageCount; p++) {
//                        page = pdd.getPage(p);
//                        PDPageContentStream contentStream = new PDPageContentStream(pdd, page, PDPageContentStream.AppendMode.APPEND, true, true);
//                        contentStream.setGraphicsStateParameters(pdfExtState);
//
//                        // 设置水印字体颜色
//                        //final int[] color = {0, 0, 0, 210};
//                        contentStream.setNonStrokingColor(colorRGB);
//                        // contentStream.setNonStrokingColor(color[0], color[1], color[2], color[3]);
//                        contentStream.beginText();
//                        contentStream.setFont(font, fontSize);
//
//                        // 根据纸张大小添加水印
//                        for (int h = 0; h < pageHeight; h = h + (int) rotateHeight + 20) {
//                            for (int w = 0; w < pageWidth; w = w + (int) rotateWidth + 20) {
//                                try {
//                                    contentStream.setTextMatrix(Matrix.getRotateInstance(Math.toRadians(deg), w, h));
//
//                                } catch (IOException ex) {
//                                    throw new RuntimeException(ex);
//                                }
//                                try {
//                                    contentStream.showText(value);
//                                } catch (IOException ex) {
//                                    throw new RuntimeException(ex);
//                                }
//                            }
//                        }
//                        // 结束渲染，关闭流
//                        contentStream.endText();
//                        contentStream.restoreGraphicsState();
//                        contentStream.close();
//                    }
//                }catch (NoSuchFieldException ex) {
//                    throw new RuntimeException(ex);
//                } catch (ClassNotFoundException ex) {
//                    throw new RuntimeException(ex);
//                } catch (IllegalAccessException ex) {
//                    throw new RuntimeException(ex);
//                } catch (InstantiationException ex) {
//                    throw new RuntimeException(ex);
//                } catch (FontFormatException ex) {
//                    throw new RuntimeException(ex);
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//
//
//            });

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
    public static void setWatermark(PDDocument pdd, String value, String style,  InputStream fontIs) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException, FontFormatException {
        String opacityStr = CSSParser.getSingleStylePropertyValue(style, "opacity");
        String colorStr = CSSParser.getSingleStylePropertyValue(style, "color");
        String degStr = CSSParser.getSingleStylePropertyValue(style, "transform");

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
                //int color = (int)Long.parseLong(colorStr, 16);
                int r = Integer.parseInt(rgb[0].trim());
                int g = Integer.parseInt(rgb[1].trim());
                int b = Integer.parseInt(rgb[2].trim());
                colorRGB = new Color(r, g, b);
            } else {
                Class objectName = Class.forName("java.awt.Color." + colorStr.toUpperCase());
                colorRGB = (Color) objectName.newInstance();
            }
        }

        setWatermark(pdd,value,deg, opacity, colorRGB, fontIs,fontSize);
    }
    public static void setWatermark(PDDocument pdd, String value, float deg, float opactity, Color colorRGB, InputStream fontIs, float fontSize) throws IOException, FontFormatException {
        byte[] fontB = IoUtil.toByteArray(fontIs);
        InputStream fontIs1 = new ByteArrayInputStream(fontB);
        InputStream fontIs2 = new ByteArrayInputStream(fontB);

        PDFont font = PDType0Font.load(pdd, fontIs1, true);
        PDExtendedGraphicsState pdfExtState = new PDExtendedGraphicsState();

        // 设置透明度
        pdfExtState.setNonStrokingAlphaConstant(opactity);
        pdfExtState.setAlphaSourceFlag(true);
        pdfExtState.getCOSObject().setItem(COSName.MASK, COSName.MULTIPLY);

        Font font2d;
        Font parent = Font.createFont(Font.TRUETYPE_FONT, fontIs2);
        font2d = parent.deriveFont(fontSize);
        Graphics2D g2d ;
        // 旋转后的矩形宽高
        // width = w*cosα + h*sinα
        // height = h*cosα + w*sinα
        float fontWidth = 0;
        float fontHeight = 0;
        float rotateWidth = 0;
        float rotateHeight = 0;

        int pageCount = pdd.getNumberOfPages();
        PDPage page;

        for (int p = 0; p < pageCount; p++) {
            page = pdd.getPage(p);
            g2d = new PdfBoxGraphics2D(pdd, pdd.getPage(p).getMediaBox());
            Rectangle2D bounds = font2d.getStringBounds(value, g2d.getFontRenderContext());
            fontWidth = (float) bounds.getWidth();
            fontHeight = (float) bounds.getHeight();
            rotateWidth = fontWidth * (float) Math.cos(Math.toRadians(deg)) + fontHeight * (float) Math.sin(Math.toRadians(deg));
            rotateHeight = fontHeight * (float) Math.cos(Math.toRadians(deg)) + fontWidth * (float) Math.sin(Math.toRadians(deg));
            PDPageContentStream contentStream = new PDPageContentStream(pdd, page, PDPageContentStream.AppendMode.APPEND, true, true);
            contentStream.setGraphicsStateParameters(pdfExtState);

            // 设置水印字体颜色
            //final int[] color = {0, 0, 0, 210};
            contentStream.setNonStrokingColor(colorRGB);
            // contentStream.setNonStrokingColor(color[0], color[1], color[2], color[3]);
            contentStream.beginText();
            contentStream.setFont(font, fontSize);

            // 根据纸张大小添加水印
            for (int h = 10; h < page.getMediaBox().getHeight(); h = h + (int) rotateHeight + 20) {
                for (int w = -10; w < page.getMediaBox().getWidth(); w = w + (int) rotateWidth + 20) {
                    try {
                        contentStream.setTextMatrix(Matrix.getRotateInstance(Math.toRadians(deg), w, h));

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        contentStream.showText(value);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            // 结束渲染，关闭流
            contentStream.endText();
            contentStream.restoreGraphicsState();
            contentStream.close();
        }
    }
}
