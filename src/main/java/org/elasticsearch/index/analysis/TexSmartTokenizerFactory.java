package org.elasticsearch.index.analysis;

import com.texsmart.TexSmart;
import com.texsmart.cfg.Configuration;
import com.texsmart.lucene.TokenizerBuilder;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

/**
 * @project: elasticsearch-analysis-texsmart
 * @description: TexSmart tokenizer factory
 * @author: wei_liu
 * @create: 2020-09-09 15:10
 */
public class TexSmartTokenizerFactory extends AbstractTokenizerFactory {
    /**
     * 分词类型
     */
    private TexSmartType texSmartType;
    /**
     * 分词配置
     */
    private Configuration configuration;

    public TexSmartTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings, TexSmartType texSmartType) {
        super(indexSettings, settings, name);
        this.texSmartType = texSmartType;
        this.configuration = new Configuration(env, settings);
    }

    public static TexSmartTokenizerFactory getTexSmartTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new TexSmartTokenizerFactory(indexSettings, env, name, settings, TexSmartType.TEXSMART);
    }

    public static TexSmartTokenizerFactory getTexSmartStandardTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new TexSmartTokenizerFactory(indexSettings, env, name, settings, TexSmartType.STANDARD);
    }

    public static TexSmartTokenizerFactory getTexSmartIndexTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new TexSmartTokenizerFactory(indexSettings, env, name, settings, TexSmartType.SINGLE);
    }



    @Override
    public Tokenizer create() {
        switch (this.texSmartType) {
            case SINGLE:
                configuration.enableIndexMode(true);
                return TokenizerBuilder.tokenizer(TexSmart.newSegment().enableIndexMode(true), configuration);
            case STANDARD:
            default:
                return TokenizerBuilder.tokenizer(TexSmart.newSegment(), configuration);
        }
    }
}
