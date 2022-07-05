package ink.rayin.app.web.utils;


import ink.rayin.cloud.base.util.YamlProp;

import java.util.Properties;

public class RayinConfig {
    public static Properties properties;

    static {
        synchronized (RayinConfig.class) {
            if(properties == null){
                properties = new YamlProp("application.yml").getProperties();
            }
        }
    }
    public static String getPropertieByKey(String key){
        return properties.getProperty(key);
    }
}
