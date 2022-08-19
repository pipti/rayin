package ink.rayin.htmladapter.openhtmltopdf.factory;

import com.openhtmltopdf.extend.FSObjectDrawer;
import com.openhtmltopdf.extend.FSObjectDrawerFactory;
import org.w3c.dom.Element;

public class WatermarkDrawerFactory  implements FSObjectDrawerFactory {
    @Override
    public FSObjectDrawer createDrawer(Element e) {
        if (isReplacedObject(e)) {
           // return new WatermarkDrawer();
        }
        return null;
    }

    @Override
    public boolean isReplacedObject(Element e) {
        return e.getAttribute("type").equals("watermark");
    }
}
