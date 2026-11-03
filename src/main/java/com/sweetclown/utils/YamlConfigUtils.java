package com.sweetclown.utils;

import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class YamlConfigUtils {

    private static final Map<String, String> yamlMap = new ConcurrentHashMap<>();

    static {
        Yaml yaml = new Yaml();
        try (InputStream is = YamlConfigUtils.class.getClassLoader().getResourceAsStream("application.yaml")) {
            if (is == null) {
                System.err.println("錯誤：找不到 application.yaml 檔案！");
            } else {
                Map<String, Object> rawMap = yaml.load(is);
                if (rawMap != null) {
                    // 呼叫遞迴方法，將巢狀 Map 扁平化
                    flattenYaml("", rawMap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 💡 核心邏輯：遞迴解析多層的 YAML 結構
    private static void flattenYaml(String prefix, Map<String, Object> rawMap) {
        rawMap.forEach((key, value) -> {
            // 組合出目前層級的完整路徑，Example : "spring" + "." + "application"
            String fullKey = prefix.isEmpty() ? key : prefix + "." + key;
            if (value instanceof Map) {
                // 如果值還是個 Map，代表還有下一層，繼續往內解析
                flattenYaml(fullKey, (Map<String, Object>) value);
            } else if (value != null) {
                // 如果已經是最內層，就直接轉 String 存進 map
                yamlMap.put(fullKey, value.toString());
            }
        });
    }

    public static String getString(String key) {
        return yamlMap.get(key);
    }

    //Test Method
    public static void main(String[] args) {

        //Test for get Application name
        System.out.println(YamlConfigUtils.getString("spring.application.name"));

        //Test for get DataSource Url
        System.out.println(YamlConfigUtils.getString("spring.datasource.url"));
    }
}
