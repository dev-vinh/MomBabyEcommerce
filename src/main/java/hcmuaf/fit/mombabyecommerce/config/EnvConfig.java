package hcmuaf.fit.mombabyecommerce.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.Map;

public class EnvConfig {
    private static final Map<String, String> envVariables = new HashMap<>();
    private static final Dotenv dotenv;

    static {
        try {
            dotenv = Dotenv.configure()
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            dotenv.entries().forEach(entry ->
                    envVariables.put(entry.getKey(), entry.getValue())
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to load .env file", e);
        }
    }

    public static String get(String key) {
        String value = System.getenv(key);
        if (value != null) {
            return value;
        }
        return envVariables.get(key);
    }
}
