package ink.rayin.htmladapter.openhtmltopdf.factory;

import com.openhtmltopdf.extend.FSObjectDrawer;
import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.extend.OutputDevice;
import com.openhtmltopdf.pdfboxout.PdfBoxOutputDevice;
import com.openhtmltopdf.render.RenderingContext;
import ink.rayin.htmladapter.base.utils.CSSParser;
import ink.rayin.htmladapter.openhtmltopdf.utils.PdfBoxTools;
import ink.rayin.tools.utils.ResourceUtil;
import ink.rayin.tools.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.w3c.dom.Element;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class PdfObjectDrawer implements FSObjectDrawer {
    HashMap<String, FSSupplier<InputStream>> fontCache;

    public PdfObjectDrawer(HashMap<String, FSSupplier<InputStream>> fontCache){
        this.fontCache = fontCache;
    }

    public PdfObjectDrawer() {
    }

    @Override
    public Map<Shape, String> drawObject(Element e, double x, double y, double width, double height,
                                         OutputDevice outputDevice, RenderingContext ctx, int dotsPerPixel) {
        PDDocument pdd = ((PdfBoxOutputDevice) outputDevice).getWriter();
        String value = e.getAttribute("value");
        String pages = e.getAttribute("page");
        if(StringUtil.isBlank(value)) {
            return null;
        }
        if (value.startsWith("http://") || value.startsWith("https://") || value.startsWith("file:")) {
            if(value.startsWith("file:")){
                value = value.replace("\\", "/");
                value = value.replace("file:", "");

            }
        }else if (value.startsWith("/") || value.startsWith("\\")) {
           // value = "file:" + "//" + value;
            value = value.replace("\\" , "/");
            log.debug("pdf url convert:\'" + value + "'");
        }else{
            value = value.replace("\\" , "/");
            log.debug("pdf url convert:\'" + value + "'");
        }
        PDDocument extractDoc = null;
        try {
            extractDoc = PDDocument.load(ResourceUtil.getResourceAsStream(value));

            if(StringUtil.isNotBlank(pages)){
                String[] pageB = pages.split(",");
                for(String k : pageB){
                    pdd.addPage(extractDoc.getPage(Integer.valueOf(k)));
                }
            }else{
                extractDoc.getPages().forEach(page->{
                    pdd.addPage(page);
                });
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch(IndexOutOfBoundsException ex){
            log.error(ex.getMessage());
        }
        return null;
    }

}
