package ink.rayin.tools.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class YamlProp{
    Properties properties;
    public YamlProp(String yamlFile) {
        setYaml(yamlFile);
    }

    private void setYaml(String yamlFile){
        LinkedHashMap map = null;
        Properties properties = new Properties();
        InputStream in = null;
        try {
            Yaml yaml = new Yaml();
            in = YamlProp.class.getClassLoader().getResourceAsStream(yamlFile);
            map = yaml.loadAs(in, LinkedHashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setProperties(properties,map,"");
        this.properties = properties;
    }

    private static void setProperties(Properties properties, Map map, String prefix) {
        for (Object key :map.keySet()){
            Object value = map.get(key);
            if(key == null){
                continue;
            }
            if(value instanceof Map){
                setProperties(properties, (Map) value,prefix+key.toString()+".");
            }else {
                if(value == null){
                    continue;
                }
                properties.setProperty(prefix+key.toString(),value.toString());
            }
        }
    }
    public Properties getProperties(){
        return this.properties;
    }
}
