package ink.rayin.htmladapter.openhtmltopdf.utils;

import de.rototor.pdfbox.graphics2d.PdfBoxGraphics2D;
import ink.rayin.tools.utils.FileUtil;
import ink.rayin.tools.utils.IoUtil;
import lombok.SneakyThrows;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.util.Matrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

/**
 * PDF工具类
 *
 * @author Janah Wang / 王柱 2022-08-31
 */
public class PdfBoxTools {

    /**
     * PDF 元数据读取
     * PDF metadata reading
     * @param pdf pdf输入流
     * @return 元数据HashMap
     */
    @SneakyThrows
    public static HashMap<String, String> pdfAttrsRead(InputStream pdf){
        // 读取要合并的文档
        PDDocument document = PDDocument.load(pdf);
        PDDocumentInformation info = document.getDocumentInformation();

        HashMap pdfMeta = new HashMap<String,String>();
        pdfMeta.put("Author",info.getAuthor());
        pdfMeta.put("Creator",info.getCreator());
        pdfMeta.put("Keywords",info.getKeywords());
        pdfMeta.put("Producer",info.getProducer());
        pdfMeta.put("Subject",info.getSubject());
        pdfMeta.put("Title",info.getTitle());
        pdfMeta.put("PagesInfo",info.getCustomMetadataValue("PagesInfo"));
        document.close();
        return pdfMeta;
    }

    /**
     * PDF 模板元数据读取
     * PDF template metadata reading
     * @param pdf pdf流
     * @return json 字符串
     */
    @SneakyThrows
    public static String pdfPageInfoRead(InputStream pdf){
        HashMap pra = pdfAttrsRead(pdf);
        //写入文件相关配置信息包括页码，单模板类型以及页码起始页
        final Base64.Decoder decoder = Base64.getDecoder();
        Object pagesInfo = pra.get("PagesInfo");
        if (pagesInfo == null) {
            return null;
        }
        return new String(decoder.decode(pagesInfo.toString()),"UTF-8") ;
    }

    /**
     * 设置文字水印
      * @param pdd PDDocument
     * @param value 水印文字
     * @param deg 角度，非弧度
     * @param opactity 透明度（0-1），1为不透明
     * @param colorRGB 颜色
     * @param fontIs 字体流
     * @param fontSize 字体大小pt
     * @param margin 间距pt（角度转换后质检的间距）
     * @throws IOException
     * @throws FontFormatException
     */
    public static void setFontWatermark(PDDocument pdd, String value, float deg, float opactity, Color colorRGB, InputStream fontIs, float fontSize, float margin) throws IOException, FontFormatException {
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
            for (int h = (int)(rotateHeight/2*-1); h < page.getMediaBox().getHeight() + rotateHeight; h = h + (int) rotateHeight + (int)margin) {
                for (int w = (int)(rotateWidth/2*-1); w < page.getMediaBox().getWidth() + rotateWidth; w = w + (int) rotateWidth + (int)margin) {
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

    /**
     * 设置图片水印
     * @param pdd PDDocument
     * @param imgBos 图片
     * @param imgWidth 图片宽度，高度自动按比例缩放
     * @param deg 角度（整数，非弧度）
     * @param opactity （透明度，0-1）
     * @param margin 水印图片间距（角度转换后的间距），pt单位
     * @throws IOException
     */
    public static void setImageWatermark(PDDocument pdd, ByteArrayOutputStream imgBos, float imgWidth, float deg, float opactity, float margin) throws IOException {
        PDImageXObject pdImage = PDImageXObject.createFromByteArray(pdd, imgBos.toByteArray(), "");
        PDExtendedGraphicsState pdfExtState = new PDExtendedGraphicsState();
        float imgHeight = (imgWidth / pdImage.getWidth()) * pdImage.getHeight();

        // 设置透明度
        pdfExtState.setNonStrokingAlphaConstant(new Float(opactity));
        pdfExtState.setAlphaSourceFlag(true);
        pdfExtState.getCOSObject().setItem(COSName.MASK, COSName.MULTIPLY);


        // 旋转后的矩形宽高
        // width = w*cosα + h*sinα
        // height = h*cosα + w*sinα
        float rotateWidth = imgWidth * (float) Math.cos(Math.toRadians(deg)) + imgHeight * (float) Math.sin(Math.toRadians(deg));
        float rotateHeight = imgHeight * (float) Math.cos(Math.toRadians(deg)) + imgWidth * (float) Math.sin(Math.toRadians(deg));

        PDPage page;
        PDPageContentStream contentStream;

        int pageCount = pdd.getNumberOfPages();

        for (int p = 0; p < pageCount; p++) {
            page = pdd.getPage(p);

            contentStream = new PDPageContentStream(pdd, page, PDPageContentStream.AppendMode.APPEND, true, true);
            contentStream.setGraphicsStateParameters(pdfExtState);

            // 根据纸张大小添加水印
            for (int h = (int)(rotateHeight/2*-1); h < page.getMediaBox().getHeight() + rotateHeight; h = h + (int) rotateHeight + (int)margin) {
                for (int w = (int)(rotateWidth/2*-1); w < page.getMediaBox().getWidth() + rotateWidth; w = w + (int) rotateWidth + (int)margin) {
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
    }

    /**
     * pdf单页转换图片
     * @param pdd
     * @param pageNum
     * @param dpi
     * @param imageType
     * @return
     * @throws IOException
     */
    public static BufferedImage pageConvertToImage(PDDocument pdd, int pageNum, float dpi, ImageType imageType) throws IOException {
        PDFRenderer renderer = new PDFRenderer(pdd);
        return renderer.renderImageWithDPI(pageNum, dpi, imageType);
    }
    public static void pageConvertToImageFile(PDDocument pdd, int pageNum, float dpi, ImageType imageType,String format, String path) throws IOException {
        BufferedImage bfImg = pageConvertToImage(pdd, pageNum, dpi, imageType);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bfImg, format, outputStream);
        byte[] imgBytes = outputStream.toByteArray();
        saveFile(imgBytes, path);
    }
    /**
     * pdf转换图片列表
     * @param pdd
     * @param dpi
     * @param imageType
     * @return
     * @throws IOException
     */
    public static List<BufferedImage> pageConvertToImages(PDDocument pdd, float dpi, ImageType imageType) throws IOException {
        PDFRenderer renderer = new PDFRenderer(pdd);
        List<BufferedImage> bufferedImageList = new ArrayList<>();
        for(int p = 0;p < pdd.getNumberOfPages(); p++){
            bufferedImageList.add(renderer.renderImageWithDPI(p, dpi, imageType));
        }
        return bufferedImageList;
    }

    /**
     * pdf所有页转图片文件
     * @param pdd
     * @param dpi
     * @param imageType
     * @param format
     * @param path
     * @param formatFileName xx%dxxx
     * @throws IOException
     */
    public static void pdfConvertToImageFiles(PDDocument pdd, float dpi, ImageType imageType,String format, String path, String formatFileName) throws IOException {
        for(int p = 0; p < pdd.getNumberOfPages(); p++){
            pageConvertToImageFile(pdd, p, dpi, imageType, format,
                    path + (formatFileName.lastIndexOf("." + format) >=0 ?String.format(formatFileName, p + 1):String.format(formatFileName + "." + format, p + 1)));
        }
    }


    /**
     * 添加图片
     * @param pdd
     * @param pageNum
     * @param imgBos
     * @param imgWidth 图片宽度
     * @param deg 角度
     * @param opactity 角度
     * @param left 横坐标
     * @param bottom 纵坐标，从左下角开始算
     * @throws IOException
     */
    public static void appendImg(PDDocument pdd, int pageNum, ByteArrayOutputStream imgBos, float imgWidth, float deg, float opactity,float left,float bottom) throws IOException {
        PDPage page = pdd.getPage(pageNum);
        if(page == null){
            page = new PDPage();
            pdd.addPage(page);
        }
        appendImg(pdd, page, imgBos, imgWidth, deg, opactity, left, bottom);
    }

    /**
     * 添加图片
     * @param pdd
     * @param pdpage
     * @param imgBos
     * @param imgWidth
     * @param deg 角度
     * @param opactity 透明度
     * @param left 横坐标
     * @param bottom 纵坐标，从左下角开始算
     * @throws IOException
     */
    public static void appendImg(PDDocument pdd, PDPage pdpage, ByteArrayOutputStream imgBos, float imgWidth, float deg, float opactity,float left,float bottom) throws IOException {
        PDImageXObject pdImage = PDImageXObject.createFromByteArray(pdd, imgBos.toByteArray(), "");
        PDExtendedGraphicsState pdfExtState = new PDExtendedGraphicsState();
        float imgHeight = (imgWidth / pdImage.getWidth()) * pdImage.getHeight();

        // 设置透明度
        pdfExtState.setNonStrokingAlphaConstant(opactity);
        pdfExtState.setAlphaSourceFlag(true);
        pdfExtState.getCOSObject().setItem(COSName.MASK, COSName.MULTIPLY);

        PDPageContentStream contentStream = new PDPageContentStream(pdd, pdpage, PDPageContentStream.AppendMode.APPEND, true, true);
        contentStream.setGraphicsStateParameters(pdfExtState);

        // 添加图片
        try {
            Matrix matrix = new Matrix();
            // 位置
            matrix.translate(left, bottom);
            // 旋转角度
            matrix.rotate(Math.toRadians(deg));
            // 修正图片大小
            matrix.scale(imgWidth, imgHeight);
            // 绘制
            contentStream.drawImage(pdImage, matrix);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // 结束渲染，关闭流
        contentStream.restoreGraphicsState();
        contentStream.close();
    }

//    public static PDDocument converToImgPdf(PDDocument pdd, int dpi, ImageType imageType) throws IOException {
//        List<BufferedImage> pdfImageList = pageConvertToImage(pdd, dpi, imageType);
//        PDDocument newPdd = new PDDocument();
//        for(BufferedImage bi:pdfImageList){
//            PDPage pdpage = new PDPage();
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            ImageIO.write(bi, "png", outputStream);
//            appendImg(newPdd, pdpage, outputStream, bi.getWidth(), 0f, 1f ,0f,0f);
//        }
//        newPdd.save(baos);
//        newPdd.close();
//        return newPdd;
//    }

    /**
     * 将pdf转换为img形式的pdf
     * @param pdd
     * @param dpi
     * @param imageType
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream converToImgPdfOs(PDDocument pdd, int dpi, ImageType imageType, String format) throws IOException {
        //PDDocument pdd = PDDocument.load(pdfOs.toByteArray());
        List<BufferedImage> pdfImageList = pageConvertToImages(pdd, dpi, imageType);
        PDDocument newPdd = new PDDocument();
        for(BufferedImage bi:pdfImageList){
            PDPage pdpage = new PDPage(new PDRectangle(bi.getWidth(), bi.getHeight()));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bi, format, outputStream);
            newPdd.addPage(pdpage);
            appendImg(newPdd, pdpage, outputStream, bi.getWidth(), 0f, 1f ,0f,0f);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        newPdd.save(baos);
        newPdd.close();
        return baos;
    }

    public static void converPdfToImgPdf(PDDocument pdd, int dpi, ImageType imageType, String formate, String path) throws IOException {
        saveFile(converToImgPdfOs(pdd, dpi, imageType, formate).toByteArray(), path);
    }

    public static void saveFile(byte[] bs, String path) throws IOException {
        FileUtil.toFile(new ByteArrayInputStream(bs), new File(path));
    }
    public static void saveFile(PDDocument pd, String path) throws IOException {
        pd.save(path);
    }

    /**
     * pdf拆分
     * @param pdd 要拆分的pdf
     * @param splitePoint 拆分起始点数组，1为起始页，例如1,3,5：拆分1-2,3-4,5至最后
     * @param path 保存路径，不包含文件名
     * @param formatFileName 文件名format ，xxx%sxxx,%s是起始页到终止页1_2
     * @throws IOException
     */
    public static void pdfSplite(PDDocument pdd, int[] splitePoint, String path, String formatFileName) throws IOException {
        List<PDDocument> pds = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        for(int k = 0; k < splitePoint.length - 1; k++){
            PDDocument pdTmp = new PDDocument();
            for(int p = splitePoint[k] - 1; p < splitePoint[k + 1] - 2; p++){
                pdTmp.addPage(pdd.getPage(p));

            }
            fileNames.add(String.format(formatFileName, splitePoint[k] + "_" + splitePoint[k + 1]));
            pds.add(pdTmp);
        }
        for(int i = 0; i < pds.size(); i++){
            pds.get(i).save(path + fileNames.get(i));
        }
    }


    public static OutputStream pdfMerge(List<InputStream> sources) throws IOException {
        PDFMergerUtility PDFmerger = new PDFMergerUtility();
        PDFmerger.addSources(sources);
        return PDFmerger.getDestinationStream();
    }
}
