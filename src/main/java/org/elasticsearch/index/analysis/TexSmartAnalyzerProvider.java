package org.elasticsearch.index.analysis;

import com.texsmart.cfg.Configuration;
import com.texsmart.lucene.*;
import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

/**
 * @project: elasticsearch-analysis-texsmart
 * @description: TexSmart analyzer provider
 * @author: wei_liu
 * @create: 2020-09-09 15:10
 */
public class TexSmartAnalyzerProvider extends AbstractIndexAnalyzerProvider<Analyzer> {

    private final Analyzer analyzer;

    public TexSmartAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings, TexSmartType texSmartType) {
        super(indexSettings, name, settings);
        Configuration configuration = new Configuration(env, settings);
        switch (texSmartType) {
            case TEXSMART:
                analyzer = new TexSmartAnalyzer(configuration);
                break;
            case STANDARD:
                analyzer = new TexSmartStandardAnalyzer(configuration);
                break;
            case SINGLE:
                analyzer = new TexSmartIndexAnalyzer(configuration);
                break;
            default:
                analyzer = null;
                break;
        }
    }

    public static TexSmartAnalyzerProvider getTexSmartAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new TexSmartAnalyzerProvider(indexSettings, env, name, settings, TexSmartType.TEXSMART);
    }

    public static TexSmartAnalyzerProvider getTexSmartStandardAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new TexSmartAnalyzerProvider(indexSettings, env, name, settings, TexSmartType.STANDARD);
    }

    public static TexSmartAnalyzerProvider getTexSmartIndexAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        return new TexSmartAnalyzerProvider(indexSettings, env, name, settings, TexSmartType.SINGLE);
    }

    @Override
    public Analyzer get() {
        return this.analyzer;
    }
}
