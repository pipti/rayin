package ink.rayin.app.web.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;


/**
 * [organization Model].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-04-01 WangZhu created<br>
 * <br>
 * @author WangZhu
 * @version 1.0
 * @since JDK 1.8
 */
@Data
//@SuperBuilder
//@AllArgsConstructor
//@NoArgsConstructor
@Accessors(chain = true)
public class Organization implements Serializable {
    @TableId(type = IdType.ASSIGN_UUID)
    private String organizationId;
    private String organizationName;
    private int orgNumber = 10;
    private String icon;
    private String iconColor;
    private String accessKey;
    private String secretKey;

    private String thirdStorageAccessKey;
    private String thirdStorageSecretKey;
    private String thirdStorageBucket;
    private String thirdStorageResourceBucket;
    private String thirdStorageUrl;
    private String ossType;
    private Date createTime;
    private boolean deposit;
}
