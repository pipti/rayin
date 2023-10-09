package ink.rayin.htmladapter.openhtmltopdf.factory;

import com.openhtmltopdf.extend.FSObjectDrawer;
import com.openhtmltopdf.extend.OutputDevice;
import com.openhtmltopdf.pdfboxout.PdfBoxOutputDevice;
import com.openhtmltopdf.render.RenderingContext;
import ink.rayin.htmladapter.base.utils.CSSParser;
import ink.rayin.tools.utils.ImageUtil;
import ink.rayin.tools.utils.ResourceUtil;
import ink.rayin.tools.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jonah Wang
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

        PDPage page = ((PdfBoxOutputDevice) outputDevice).getPage();
        try {
            PDPageContentStream contentStream = new PDPageContentStream(((PdfBoxOutputDevice) outputDevice).getWriter(), page, PDPageContentStream.AppendMode.APPEND, true, true);

            String opacity = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"),"opacity");

            String degStr = CSSParser.getSingleStylePropertyValue(e.getAttribute("style"),"transform");
            String src = e.getAttribute("src");
            if(StringUtil.isBlank(src)){
                return null;
            }
            ByteArrayOutputStream imgBos = ResourceUtil.getResourceAsByte(src);
            //log.debug("imgBos.size()"+imgBos.size());
            if(imgBos.size()/1024 > 30){
                log.warn("水印图片有点大噢！");
            }
            if(StringUtil.isBlank(opacity)){
                opacity = "0.5";
            }

            PDImageXObject pdImage = PDImageXObject.createFromByteArray(((PdfBoxOutputDevice) outputDevice).getWriter(), imgBos.toByteArray(), "");

            PDExtendedGraphicsState pdfExtState = new PDExtendedGraphicsState();

            // 设置透明度
            pdfExtState.setNonStrokingAlphaConstant(Float.parseFloat(opacity));
            pdfExtState.setAlphaSourceFlag(true);
            pdfExtState.getCOSObject().setItem(COSName.MASK, COSName.MULTIPLY);
            contentStream.setGraphicsStateParameters(pdfExtState);

//            float imgWidth = (float)pdImage.getWidth();
//            float imgHeight = (float)pdImage.getHeight();
            float imgWidth = 90;
            float imgHeight = 10;
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
            deg = 10;
            float rotateWidth = imgWidth * (float)Math.cos(Math.toRadians(deg)) + imgHeight * (float)Math.sin(Math.toRadians(deg));
            float rotateHeight = imgHeight * (float)Math.cos(Math.toRadians(deg)) + imgWidth * (float)Math.sin(Math.toRadians(deg));

            // 根据纸张大小添加水印
            for (int h = 0; h < pageHeight; h = h + (int)rotateHeight + 30) {
                for (int w = 0; w < pageWidth; w = w + (int)rotateWidth + 30) {
                    try {

                   //     contentStream.drawImage(pdImage, w, h );

                        //contentStream.setTextMatrix(Matrix.getRotateInstance(Math.toRadians(deg), w, h));
                       // AffineTransform at = new AffineTransform(ximage.getHeight() / 2, 0, 0, ximage.getWidth() / 2, x + ximage1.getWidth(), y);

                        AffineTransform at = new AffineTransform(rotateWidth -20 , 0, 0, rotateHeight, w , h );

                        at.rotate(Math.toRadians(deg));

                        contentStream.drawXObject(pdImage, at);

                       // contentStream.drawImage(pdImage, w, h, rotateWidth, rotateHeight);



                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }

            // 结束渲染，关闭流

            contentStream.restoreGraphicsState();
            contentStream.close();

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
