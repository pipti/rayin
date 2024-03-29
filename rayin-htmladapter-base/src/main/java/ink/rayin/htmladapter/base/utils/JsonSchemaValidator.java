/**
 * Copyright (c) 2022-2030, Janah Wang / 王柱 (wangzhu@cityape.tech).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    /**
     * getJsonNodeFromString
     * @param jsonStr json String
     * @return JsonNode
     */
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

    /**
     * getJsonNodeFromFile
     * @param filePath filePath
     * @return JsonNode
     */
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

    /**
     * getJsonNodeFromInputStream
     * @param is InputStream
     * @return JsonNode
     */
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
