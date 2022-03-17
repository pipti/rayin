package ink.rayin.htmladapter.openhtmltopdf.factory;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

/**
 * RenderBuilder
 *
 * @author Jonah Wang
 */
public class OpenhttptopdfRenderBuilder {
    PdfRendererBuilder pdfRendererBuilder;

    private boolean active;
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPdfRendererBuilder(PdfRendererBuilder pdfRendererBuilder){
        this.pdfRendererBuilder = pdfRendererBuilder;
    }
    public PdfRendererBuilder getPdfRendererBuilder(){
        return pdfRendererBuilder;
    }
}
