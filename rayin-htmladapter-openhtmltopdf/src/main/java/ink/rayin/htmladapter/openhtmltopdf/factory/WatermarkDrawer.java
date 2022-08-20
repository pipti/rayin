package ink.rayin.htmladapter.openhtmltopdf.factory;

import com.openhtmltopdf.extend.FSObjectDrawer;
import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.extend.OutputDevice;
import com.openhtmltopdf.render.RenderingContext;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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
        outputDevice.drawWithGraphics((float) x, (float) y, (float) width / dotsPerPixel,
                (float) height / dotsPerPixel, (Graphics2D g2d) -> {

                    double realWidth = width / dotsPerPixel;
                    double realHeight = height / dotsPerPixel;


                    Font font;
                    try {
                        Font parent = Font.createFont(Font.TRUETYPE_FONT, fontCache.get("FangSong").supply());
                        //Font parent = Font.createFont(Font.TRUETYPE_FONT, new File("/Users/eric/Documents/dev_projects/opensource/rayin/rayin-htmladapter-base/src/main/resources/rayin_default_fonts/FangSong.ttf"));
                        font = parent.deriveFont(20f);
                    } catch (FontFormatException | IOException e1) {
                        e1.printStackTrace();
                        throw new RuntimeException(e1);
                    }

                    Rectangle2D bounds = font.getStringBounds(e.getAttribute("value"), g2d.getFontRenderContext());

                    g2d.setFont(font);
                    g2d.setPaint(Color.RED);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                    int rowCount = new Double(realWidth/bounds.getWidth()).intValue();
                    int colCount = new Double(realHeight/bounds.getHeight()).intValue();

                        for(int i = 0; i< colCount; i++){
                            for(int j = 0; j< rowCount; j++){
                                g2d.drawString(e.getAttribute("value"),
                                        (float) (i*bounds.getWidth()),
                                        (float) (j*bounds.getHeight()));
                            }
                         }


                });

        return null;
    }
}
