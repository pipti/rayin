package ink.rayin.htmladapter.base.model.tplconfig;

import lombok.Data;

@Data
public class WatermarkProperty {
    public static final String FONT_WATERMARK="font_watermark";
    public static final String IMG_WATERMARK="img_watermark";

    /**
     * 水印类型
     */
    String type;
    /**
     * 水印样式
     */
    String style;
    /**
     * 水印文字或路径
     */
    String value;

}
