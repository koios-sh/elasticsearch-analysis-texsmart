package com.texsmart.dic;

import com.texsmart.cfg.Configuration;
import com.texsmart.dic.cache.DictionaryFileCache;
import com.texsmart.dic.config.RemoteDictConfig;
import org.elasticsearch.plugin.analysis.texsmart.AnalysisTexSmartPlugin;

import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @project: elasticsearch-analysis-texsmart
 * @description: 词典类
 * @author: wei_liu
 * @create: 2020-09-09 15:10
 */
public class Dictionary {
    /**
     * 词典单子实例
     */
    private static Dictionary singleton;
    /**
     * TexSmart配置文件名
     */
    public static final String CONFIG_FILE_NAME = "texsmart.properties";
    /**
     * TexSmart远程词典配置文件名
     */
    private static final String REMOTE_CONFIG_FILE_NAME = "texsmart-remote.xml";

    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    private Dictionary(Configuration configuration) {
        Path configDir = configuration.getEnvironment().configFile().resolve(AnalysisTexSmartPlugin.PLUGIN_NAME);
        DictionaryFileCache.configCachePath(configuration);
        DictionaryFileCache.loadCache();
        RemoteDictConfig.initial(configDir.resolve(REMOTE_CONFIG_FILE_NAME).toString());
    }

    public static synchronized Dictionary initial(Configuration configuration) {
        if (singleton == null) {
            synchronized (Dictionary.class) {
                if (singleton == null) {
                    singleton = new Dictionary(configuration);
                    pool.scheduleAtFixedRate(new ExtMonitor(), 10, 60, TimeUnit.SECONDS);
                    if (configuration.isEnableRemoteDict()) {
                        for (String location : RemoteDictConfig.getSingleton().getRemoteExtDictionarys()) {
                            pool.scheduleAtFixedRate(new RemoteMonitor(location, "custom"), 10, 60, TimeUnit.SECONDS);
                        }

                        for (String location : RemoteDictConfig.getSingleton().getRemoteExtStopWordDictionarys()) {
                            pool.scheduleAtFixedRate(new RemoteMonitor(location, "stop"), 10, 60, TimeUnit.SECONDS);
                        }
                    }
                    return singleton;
                }
            }
        }
        return singleton;
    }
}
