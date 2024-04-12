package ink.rayin.htmladapter.base.model.tplconfig;


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
