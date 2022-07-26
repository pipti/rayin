
package ink.rayin.app.web.service;

import com.alibaba.fastjson2.JSONObject;
import ink.rayin.app.web.model.BaseParam;
import ink.rayin.app.web.model.Rayin;

import java.io.InputStream;
import java.util.Map;

/**
 * [接口pdf创建服务类].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-03-09 Wang Zhu create<br>
 * <br>
 * @author Wang Zhu
 * @version 1.0
 * @since JDK 1.8
 */
public interface IRestPdfCreateService {


    Map<String,String> createPdfByTemplateId(String accessKey, Rayin rayin);

    void createPdfByTemplateIdAsync(String accessKey, Rayin rayin);
    void init() throws Exception;
//    JSONObject fileCheck (InputStream inputStream) throws Exception;
}
