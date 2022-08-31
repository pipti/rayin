package ink.rayin.htmladapter.openhtmltopdf.utils;

import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;

public class PdfBoxTools {
    /**
     * PDF 元数据读取
     * PDF metadata reading
     * @param pdf pdf输入流
     * @return 元数据HashMap
     * 2020-01-07
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
     * 2020-01-07
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
}
