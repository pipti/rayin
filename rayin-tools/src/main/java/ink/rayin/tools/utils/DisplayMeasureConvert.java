package ink.rayin.tools.utils;

import org.apache.commons.lang.StringUtils;

/**
 * px pt in单位转换
 *
 * @author Jonah wz
 * @date 2022-03-14
 */
public class DisplayMeasureConvert {


    /**
     * pixelsToPoints
     * @param pixels
     * @param dpi
     * @return
     */
    public static float pixelsToPoints(float pixels, int dpi) {
        return pixels / dpi * 72;
    }

    /**
     * pixelsToCm
     * @param pixels
     * @param dpi
     * @return
     */
    public static float pixelsToCm(float pixels, int dpi) {
        return (pixels / dpi) * (1 / 0.393700787f);
    }

    /**
     * cmToPixels
     * @param cm
     * @param dpi
     * @return
     */
    public static float cmToPixels(float cm, int dpi) {
        return (cm/(1 / 0.393700787f)) * dpi;
    }

    /**
     * pixelsToInches
     * @param pixels
     * @param dpi
     * @return
     */
    public static float pixelsToInches(float pixels, int dpi) {
        return pixels / dpi;
    }

    /**
     * InchesToPixels
     * @param inches
     * @param dpi
     * @return
     */
    public static float InchesToPixels(float inches, int dpi) {
        return inches * dpi;
    }

    /**
     * cmToPoints
     * @param cm
     * @param dpi
     * @return
     */
    public static float cmToPoints(float cm, int dpi){
        return pixelsToPoints(cmToPixels(cm, dpi),dpi);
    }

    /**
     * inchesToPoints
     * @param cm
     * @param dpi
     * @return
     */
    public static float inchesToPoints(float cm, int dpi){
        return pixelsToPoints(InchesToPixels(cm, dpi),dpi);
    }

    /**
     * web单位转换PDF单位
     * @param meas
     * @return
     */
    public static float webMeasureToPDFPoint(String meas) {
        int DPI = 96;
        String lower = meas.toLowerCase();
        if (meas.trim().length() < 3) {
            return Float.valueOf(meas);
        }
        if(StringUtils.isNumeric(meas)){
            return Float.valueOf(meas);
        }
        String mes = meas.substring(lower.length() - 2, lower.length());
        float f = Float.valueOf(lower.substring(0, lower.length() - 2));

        switch (mes) {
            case "px":
                return pixelsToPoints(f, DPI);
            case "cm":
                return cmToPoints(f, DPI);
            case "in":
                return inchesToPoints(f, DPI);
            default:
                return f;
        }
    }

    public static void main(String[] args){
        System.out.println(webMeasureToPDFPoint("20.5cm"));
    }

}
