package ink.rayin.htmladapter.openhtmltopdf.factory;

import com.openhtmltopdf.extend.FSObjectDrawer;
import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.extend.OutputDevice;
import com.openhtmltopdf.pdfboxout.PdfBoxOutputDevice;
import com.openhtmltopdf.render.RenderingContext;
import ink.rayin.tools.utils.ResourceUtil;
import ink.rayin.tools.utils.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.w3c.dom.Element;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;



public class PdfObjectDrawer implements FSObjectDrawer {
    private static Log log = LogFactory.getLog(PdfObjectDrawer.class);

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
