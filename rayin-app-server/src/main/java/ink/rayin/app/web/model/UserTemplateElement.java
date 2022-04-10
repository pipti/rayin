package ink.rayin.app.web.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * [user_template_element Model].
 * []
 * <h3>version info：</h3><br>
 * v1.0 2020-02-25 WangZhu created<br>
 * <br>
 * @author WangZhu
 * @version 1.0
 * @since JDK 1.8
 */
@Data
public class UserTemplateElement implements Serializable {

    String organizationId;
    String templateId;
    String templateVersion;
    String elementId;
    String elementVersion;
    String name;
    String seq;
    String elementType;
    String elementThum;
    String memo;
    String testData;
    @TableField(exist = false)
    boolean delFlag;
    boolean pageNumFirstPage;
    boolean pageNumIsCalculate;
    boolean pageNumIsDisplay;
    boolean addBlankPage;
    boolean uncommonCharsAnalysis;
    Date createTime;
    String elementAvaliableDataPath;
    @TableField(exist = false)
    List<PageNumDisplayPos> pageNumDisplayPoss;
    @TableField(exist = false)
    String content;
    @TableField(exist = false)
    String createTimeStr;
    @TableField(exist = false)
    String updateTimeStr;

    public String getCreateTimeStr(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(createTime != null){
            return dateFormatter.format(createTime);
        }
        return null;
    }

}
