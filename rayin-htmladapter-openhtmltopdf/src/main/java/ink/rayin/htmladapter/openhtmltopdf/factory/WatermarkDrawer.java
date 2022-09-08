package ink.rayin.htmladapter.openhtmltopdf.factory;

import com.openhtmltopdf.extend.FSObjectDrawer;
import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.extend.OutputDevice;
import com.openhtmltopdf.pdfboxout.PdfBoxOutputDevice;
import com.openhtmltopdf.render.RenderingContext;
import ink.rayin.htmladapter.base.utils.CSSParser;
import ink.rayin.tools.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Deprecated
public class WatermarkDrawer implements FSObjectDrawer {
    HashMap<String, FSSupplier<InputStream>> fontCache;

    public WatermarkDrawer(HashMap<String, FSSupplier<InputStream>> fontCache){
        this.fontCache = fontCache;
    }

    public WatermarkDrawer() {
    }

    @Override
    public Map<Shape, String> drawObject(Element e, double x, double y, double width, double height,
                                         OutputDevice outputDevice, RenderingContext ctx, int dotsPerPixel) {
        PdfBoxOutputDevice pdfBoxOutputDevice = (PdfBoxOutputDevice) outputDevice;
        float pageHeight = pdfBoxOutputDevice.getPage().getMediaBox().getHeight();
        float pageWidth = pdfBoxOutputDevice.getPage().getMediaBox().getWidth();

//        try {
//            CSSStyleDeclaration cssStyleDeclaration = CSSParser.addSingleStyleProperty(e.getAttribute("style"), "position","absolute", null);
//            cssStyleDeclaration = CSSParser.addSingleStyleProperty(cssStyleDeclaration, "left","500",null);
//            cssStyleDeclaration = CSSParser.addSingleStyleProperty(cssStyleDeclaration, "right","500",null);
//            e.setAttribute("style", cssStyleDeclaration.getCssText());
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

        outputDevice.drawWithGraphics(0 , 0, (float) pageWidth*2,
                (float) pageHeight*2, (Graphics2D g2d) -> {

                    double realWidth = width / dotsPerPixel;
                    double realHeight = height / dotsPerPixel;
                    float fontSize = 20f;
                    Font font;
                    Color colorRGB = Color.RED;
                    try {
                        String fontStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"),"font-family");
                        if(StringUtil.isBlank(fontStr)){
                            fontStr = "FangSong";
                        }
                        String colorStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"),"color");

                        if(colorStr.toLowerCase().indexOf("rgb") < 0 && colorStr.toLowerCase().indexOf("#") < 0){
                            Field field = Class.forName("java.awt.Color").getField(colorStr);
                            colorRGB =  (Color)field.get(null);
                        }else{
                            if(colorStr.toLowerCase().indexOf("#") >= 0){
                                colorRGB = Color.getColor(colorStr);
                            }else if(colorStr.toLowerCase().indexOf("rgb") >= 0){
                                String[] rgb = colorStr.substring(4,colorStr.length() - 1).split(",");
//
                                //int color = (int)Long.parseLong(colorStr, 16);
                                int r = Integer.parseInt(rgb[0].trim());
                                int g = Integer.parseInt(rgb[1].trim());
                                int b = Integer.parseInt(rgb[2].trim());
                                colorRGB = new Color(r,g,b);
                            }
                        }
//                        if(StringUtil.isNotBlank(colorStr)){
//                            String[] rgb = colorStr.substring(4,colorStr.length() - 1).split(",");
//
//                            //int color = (int)Long.parseLong(colorStr, 16);
//                            int r = Integer.parseInt(rgb[0].trim());
//                            int g = Integer.parseInt(rgb[1].trim());
//                            int b = Integer.parseInt(rgb[2].trim());
//                            colorRGB = new Color(r,g,b);
//                        }

                        String fontSizeStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"),"font-size");

                        // TODO

                        if(StringUtil.isNotBlank(fontSizeStr)){
                            if(fontSizeStr.indexOf("px") > 0){
                                fontSizeStr = fontSizeStr.replace("px","");
                                fontSize = Float.parseFloat(fontSizeStr)/dotsPerPixel;
                            }
                            if(fontSizeStr.indexOf("pt") > 0){
                                fontSizeStr = fontSizeStr.replace("pt","");
                                fontSize = Float.parseFloat(fontSizeStr);
                            }
                            fontSize = Float.parseFloat(fontSizeStr);
                        }
                        Font parent = Font.createFont(Font.TRUETYPE_FONT, fontCache.get(fontStr).supply());

                        //Font parent = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/eric/Documents/dev_projects/opensource/rayin/rayin-htmladapter-base/src/main/resources/rayin_default_fonts/FangSong.ttf"));
                        font = parent.deriveFont(fontSize);
                    } catch (FontFormatException | IOException e1) {
                        e1.printStackTrace();
                        throw new RuntimeException(e1);
                    } catch (NoSuchFieldException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }

                    Rectangle2D bounds = font.getStringBounds(e.getAttribute("value"), g2d.getFontRenderContext());

                    String opacityStr = null;
                    try {
                        opacityStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"),"opacity");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    float opacity = 0.3f;
                    if(StringUtil.isNumeric(opacityStr)){
                        opacity = Float.parseFloat(opacityStr);
                    }

                    g2d.setFont(font);
                    g2d.setPaint(colorRGB);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                    int colCount = new Double(pageWidth/bounds.getWidth()*2).intValue();
                    int rowCount = new Double(pageHeight/bounds.getHeight()*2).intValue();

                        for(int i = 0; i< rowCount; i++){
                            for(int j = 0; j< colCount; j++){
                                g2d.drawString(e.getAttribute("value"),
                                        (float) (j * bounds.getWidth() * 2),
                                        (float) (i * bounds.getHeight() * 2));
                            }
                         }


                });

        return null;
    }
}
