package com.texsmart.dic.config;

import com.texsmart.dic.Dictionary;
import org.elasticsearch.plugin.analysis.texsmart.AnalysisTexSmartPlugin;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class TexSmartConfig {

    private static TexSmartConfig ourInstance = new TexSmartConfig();

    private static Properties config;

    static {
        Path filePath = Paths.get(System.getProperty("user.dir"), "plugins",
                AnalysisTexSmartPlugin.PLUGIN_NAME, Dictionary.CONFIG_FILE_NAME);
        try {
            InputStream in = new FileInputStream(filePath.toString());
            config = new Properties();
            InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
            config.load(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TexSmartConfig getInstance() {
        return ourInstance;
    }

    private TexSmartConfig() {}

    public static Properties getConfig() {
        return config;
    }
}
