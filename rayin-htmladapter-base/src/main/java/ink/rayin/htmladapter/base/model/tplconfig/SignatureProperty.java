package ink.rayin.htmladapter.base.model.tplconfig;

import lombok.Getter;
import lombok.Setter;

/**
 * 签章签名信息
 *
 * @date 2019-09-08
 * @author Jonah wz
 */

public class SignatureProperty {
    public SignatureProperty(int x, int y, int pageNum, int zoomPercent, String signatureImage){
        this.x = x;
        this.y = y;
        this.pageNum = pageNum;
        this.signatureImage = signatureImage;
        this.zoomPercent = zoomPercent;
    }

    public SignatureProperty(){

    }
    @Getter
    @Setter
    private int pageNum;


    @Getter
    @Setter
    private int x;

    @Getter
    @Setter
    private int y;

    /**
     * 路径或http、https、base64
     */
    @Getter
    @Setter
    private String signatureImage;

    @Getter
    @Setter
    private int zoomPercent = -50;
}
