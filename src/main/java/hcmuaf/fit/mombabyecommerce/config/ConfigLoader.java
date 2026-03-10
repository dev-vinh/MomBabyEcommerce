package hcmuaf.fit.mombabyecommerce.config;

import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(ConfigLoader.class.getClassLoader().getResourceAsStream("application.properties"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
