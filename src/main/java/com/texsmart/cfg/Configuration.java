package com.texsmart.cfg;

import com.texsmart.dic.Dictionary;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.NerAlgType;
import org.elasticsearch.index.analysis.PosAlgType;

/**
 * @project: elasticsearch-analysis-texsmart
 * @description: 配置信息
 * @author: wei_liu
 * @create: 2020-09-09 15:10
 */
public class Configuration {

    private Environment environment;

    private Settings settings;

    private boolean enablePorterStemming;

    private boolean enableIndexMode;

    private boolean enableCustomDictionary;

    private boolean enableRemoteDict;

    private boolean enableNormalization;

    private boolean enableOffset;

    private boolean enableCustomConfig;

    private boolean enableStopDictionary;

    private PosAlgType enablePosAlg;
    private NerAlgType enableNerAlg;

    @Inject
    public Configuration(Environment env, Settings settings) {
        this.environment = env;
        this.settings = settings;
        this.enablePorterStemming = settings.get("enable_porter_stemming", "false").equals("true");
        this.enableIndexMode = settings.get("enable_index_mode", "false").equals("true");
        this.enableCustomDictionary = settings.get("enable_custom_dictionary", "true").equals("true");
        this.enableStopDictionary = settings.get("enable_stop_dictionary", "false").equals("true");
        this.enableRemoteDict = settings.get("enable_remote_dict", "true").equals("true");
        this.enableNormalization = settings.get("enable_normalization", "false").equals("true");
        this.enableOffset = settings.get("enable_offset", "true").equals("true");
        this.enableCustomConfig = settings.get("enable_custom_config", "false").equals("true");
        try {
            this.enablePosAlg = PosAlgType.valueOf(settings.get("enable_pos_alg", "log_linear"));
            this.enableNerAlg = NerAlgType.valueOf(settings.get("enable_ner_alg", "crf"));
        } catch (IllegalArgumentException e) {
            this.enablePosAlg = PosAlgType.LOG_LINEAR;
            this.enableNerAlg = NerAlgType.CRF;
        }
        Dictionary.initial(this);
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public boolean isEnablePorterStemming() {
        return this.enablePorterStemming;
    }

    public Configuration enablePorterStemming(boolean enablePorterStemming) {
        this.enablePorterStemming = enablePorterStemming;
        return this;
    }

    public boolean isEnableStopDictionary() {
        return this.enableStopDictionary;
    }

    public boolean isEnableIndexMode() {
        return this.enableIndexMode;
    }

    public Configuration enableIndexMode(boolean enableIndexMode) {
        this.enableIndexMode = enableIndexMode;
        return this;
    }

    public boolean isEnableCustomDictionary() {
        return this.enableCustomDictionary;
    }

    public Configuration enableCustomDictionary(boolean enableCustomDictionary) {
        this.enableCustomDictionary = enableCustomDictionary;
        return this;
    }

    public boolean isEnableRemoteDict() {
        return enableRemoteDict;
    }

    public Configuration enableRemoteDict(boolean enableRemoteDict) {
        this.enableRemoteDict = enableRemoteDict;
        return this;
    }

    public boolean isEnableNormalization() {
        return enableNormalization;
    }

    public Configuration enableNormalization(boolean enableNormalization) {
        this.enableNormalization = enableNormalization;
        return this;
    }

    public boolean isEnableOffset() {
        return enableOffset;
    }

    public Configuration enableOffset(boolean enableOffset) {
        this.enableOffset = enableOffset;
        return this;
    }

    public boolean isEnableCustomConfig() {
        return enableCustomConfig;
    }

    public Configuration enableCustomConfig(boolean enableCustomConfig) {
        this.enableCustomConfig = enableCustomConfig;
        return this;
    }

    public PosAlgType getEnablePosAlg() {
        return this.enablePosAlg;
    }

    public Configuration enablePosAlg(PosAlgType enablePosAlg) {
        this.enablePosAlg = enablePosAlg;
        return this;
    }

    public NerAlgType getEnableNerAlg() {
        return this.enableNerAlg;
    }

    public Configuration enablePosAlg(NerAlgType enableNerAlg) {
        this.enableNerAlg = enableNerAlg;
        return this;
    }
}
