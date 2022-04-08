package ink.rayin.test.pdfboxgenerator;

import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator;
import ink.rayin.tools.utils.EasyExcelUtils;
import ink.rayin.tools.utils.ResourceUtil;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExcelDataTest {
    static PdfGenerator pdfGenerator;
    static  {
        try {
            pdfGenerator = new PdfBoxGenerator();
            pdfGenerator.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void readExcelData() throws IOException {
        EasyExcelUtils.readWithoutHead(ResourceUtil.getResourceAsStream("data.xlsx"));
    }

    @Test
    public void generateByExcel() throws Exception {
        pdfGenerator.generatePdfFilesByEleAndExcel(ResourceUtil.getResourceAsString("examples/example2/element1.html", StandardCharsets.UTF_8),
                ResourceUtil.getResourceAsStream("data.xlsx"),"/Users/eric/Documents/dev_code/open_source/Rayin/rayin/rayin-test/tmp",
                "excel");
    }
}
