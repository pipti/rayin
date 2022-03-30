package ink.rayin.test.rules;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.base.utils.JsonSchemaValidator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.exception.ScriptEvalError;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据规则测试
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataRulesTest {
    @Test
    public void beetlTest() throws IOException, ScriptEvalError {
        Map<String, Object> paras = new HashMap<>();
        StringTemplateResourceLoader resourceLoader = new
                StringTemplateResourceLoader();
        Configuration cfg = Configuration.defaultConfiguration();
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        Template t = gt.getTemplate("hello,${name}");
        t.binding("name", "beetl");
        String str = t.render();
        System.out.println(str);

//        gt.runScript(ResourceUtil.getResource("rules/dataid_1.btl").getFile().getAbsolutePath(),
//                paras);
    }

    
}
