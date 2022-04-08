package ink.rayin.test.pdfboxgenerator;

import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator;
import ink.rayin.tools.utils.EasyExcelUtils;
import ink.rayin.tools.utils.ResourceUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExcelDataGenerateTest {
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
        EasyExcelUtils.readWithoutHead(ResourceUtil.getResourceAsStream("examples/example11/data.xlsx"));
    }

    @Test
    public void generateFilesByExcel() throws Exception {
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");
        // 生成pdf路径
        // generate pdf path
        String outputDir = new File(outputFileClass).getParentFile().getParent() + "/tmp/";
        pdfGenerator.generatePdfFilesByTplAndExcel(ResourceUtil.getResourceAsString("examples/example11/tpl.json", StandardCharsets.UTF_8),
                ResourceUtil.getResourceAsStream("examples/example11/data.xlsx"), outputDir,
                "example11_openhtmltopdf_" + System.currentTimeMillis());
    }

    @Test
    public void generateFileByExcel() throws Exception {
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");
        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example11_openhtmltopdf_" + System.currentTimeMillis() + ".pdf";
        pdfGenerator.generatePdfFileByTplAndExcel(ResourceUtil.getResourceAsString("examples/example11/tpl.json", StandardCharsets.UTF_8),
                ResourceUtil.getResourceAsStream("examples/example11/data.xlsx"),outputFile);
    }

}
