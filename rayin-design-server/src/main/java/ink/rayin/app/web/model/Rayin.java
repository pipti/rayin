package ink.rayin.app.web.model;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;

/**
 * [Json 参数].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-03-09 WangZhu created<br>
 * <br>
 * @author WangZhu
 * @version 1.0
 * @since JDK 1.8
 */
@Data
public class Rayin {
    /** "rayin":{
     "organizationId":"",
     "templateId":"",
     "templateVersion":"",
     "fileName":"",
     "timeStamp":1579404181322,
     "transactionNo":"交易流水号",
     "traceId":"跟踪流水号",
     "callbackUrl":"",
     "sysId":"xx",
     "templateAlias":"xx"
     }*/
//    @NotEmpty(message = "organizationId is not blank")
//    private String organizationId;


    private String templateId;
    @NotEmpty(message = "templateVersion is not blank")
    @DecimalMin(value = "0.01") // 版本号最小值0.01
    @DecimalMax(value = "100.00") // 版本号最大值100.00
    private String templateVersion;
    @NotEmpty(message = "timeStamp is not blank")
    private String timeStamp;
    @NotEmpty(message = "transactionNo is not blank")
    private String transactionNo;
//    @NotEmpty(message = "traceId is not blank")
    private String traceId;
    @NotEmpty(message = "callbackUrl is not blank")
    private String callbackUrl;
    private String fileName;
    @NotEmpty(message = "sysId is not blank")
    private String sysId;
    /**
     * 模板别名
     * 别名可取代模板编号
     */
    private String templateAlias;

    private String mobile;
    private String address;
    private String email;
    private String contactName;
    private String mediaSwitch;
    private String data;
    private String encryption;
}
