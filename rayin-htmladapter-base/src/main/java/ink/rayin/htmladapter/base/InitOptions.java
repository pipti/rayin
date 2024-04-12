package ink.rayin.htmladapter.base;

import java.util.ArrayList;

/**
 * 初始化参数
 *
 *
 * @author Janah Wang / 王柱 2023-10-28
 *
 * @version 1.0
 * @since 1.8
 */

public class InitOptions {
    /**
     * 最小线程
     */
    int minIdle = 2;

    /**
     * 最大空闲
     */
    int maxIdle = 1;;

    /**
     * 最大线程总数
     */
    int maxTotal = 2;;

    /**
     * 自定义字体目录，可空
     */
    String customizeFontPathDirectory;

    /**
     * 使用chromium插件时有效
     * chromium 安装路径，可空
     */
    String bPath;
    /**
     * chromium版本 可空
     */
    String cVersion = "1133621";

    /**
     * 使用chromium插件时有效
     * chromium 参数，参见 https://peter.sh/experiments/chromium-command-line-switches/
     */
    ArrayList<String> bOptions;
    ArrayList<String> pOptions;

    /**
     *
     * @param minIdle 最小线程
     * @param maxIdle 最大空闲
     * @param maxTotal 最大线程总数
     * @param customizeFontPathDirectory  自定义字体目录，可空
     * @param bPath  使用chromium插件时有效  chromium 安装路径，可空
     */
    public  InitOptions(int minIdle,int maxIdle,int maxTotal, String customizeFontPathDirectory, String bPath){
        this.minIdle = minIdle;
        this.maxIdle = maxIdle;
        this.maxTotal = maxTotal;
        this.customizeFontPathDirectory = customizeFontPathDirectory;
        this.bPath = bPath;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public String getCustomizeFontPathDirectory() {
        return customizeFontPathDirectory;
    }

    public void setCustomizeFontPathDirectory(String customizeFontPathDirectory) {
        this.customizeFontPathDirectory = customizeFontPathDirectory;
    }

    public String getbPath() {
        return bPath;
    }

    public void setbPath(String bPath) {
        this.bPath = bPath;
    }

    public String getcVersion() {
        return cVersion;
    }

    public void setcVersion(String cVersion) {
        this.cVersion = cVersion;
    }

    public ArrayList<String> getbOptions() {
        return bOptions;
    }

    public void setbOptions(ArrayList<String> bOptions) {
        this.bOptions = bOptions;
    }

    public ArrayList<String> getpOptions() {
        return pOptions;
    }

    public void setpOptions(ArrayList<String> pOptions) {
        this.pOptions = pOptions;
    }
}
