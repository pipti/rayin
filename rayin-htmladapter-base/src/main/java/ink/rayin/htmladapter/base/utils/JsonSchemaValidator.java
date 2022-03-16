package ink.rayin.htmladapter.base.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.JsonNodeReader;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonSchemaValidator {

    private static Logger logger = LoggerFactory.getLogger(JsonSchemaValidator.class);

    /**
     * json schema校验
     * @param jsonNode 待校验的json
     * @param schemaNode json格式规则描述
     * @return
     */
    public static void validateJsonByFgeByJsonNode(JsonNode jsonNode, JsonNode schemaNode) {
        Map<String, Object> result = new HashMap<String, Object>();
        ProcessingReport report = null;
        report = JsonSchemaFactory.byDefault().getValidator().validateUnchecked(schemaNode, jsonNode);
        if (report.isSuccess()) {
        } else {
            Iterator<ProcessingMessage> it = report.iterator();
            String ms = "";
            while (it.hasNext()) {
                ProcessingMessage pm = it.next();
                if (!LogLevel.WARNING.equals(pm.getLogLevel())) {
                    ms += pm;
                }

            }
            logger.error("校验失败！" + ms);
            throw new RayinException(CodeMessage.DATA_FORMAT_ERROR,ms,null);

           // return result;
        }
    }

    public static JsonNode getJsonNodeFromString(String jsonStr) {
        JsonNode jsonNode = null;
        try {
            jsonNode = JsonLoader.fromString(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RayinException("模板配置格式不正确，请确认配置是否正确！");
        }
        return jsonNode;
    }

    public static JsonNode getJsonNodeFromFile(String filePath) {
        JsonNode jsonNode = null;
        try {
            jsonNode = new JsonNodeReader().fromReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode;
    }

    public static JsonNode getJsonNodeFromInputStream(InputStream is) {
        JsonNode jsonNode = null;
        try {
            jsonNode = new JsonNodeReader().fromInputStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode;
    }

}
