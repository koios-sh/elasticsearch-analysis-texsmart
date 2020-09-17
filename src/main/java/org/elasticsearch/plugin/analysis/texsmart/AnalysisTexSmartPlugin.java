package org.elasticsearch.plugin.analysis.texsmart;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.SpecialPermission;
import org.elasticsearch.index.analysis.AnalyzerProvider;
import org.elasticsearch.index.analysis.TexSmartAnalyzerProvider;
import org.elasticsearch.index.analysis.TexSmartTokenizerFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

/**
 * @project: elasticsearch-analysis-texsmart
 * @description: TexSmart分词插件
 * @author: wei_liu
 * @create: 2018-12-14 15:10
 */
public class AnalysisTexSmartPlugin extends Plugin implements AnalysisPlugin {
    public static String PLUGIN_NAME = "analysis-texsmart";

    static {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            // unprivileged code such as scripts do not have SpecialPermission
            sm.checkPermission(new SpecialPermission());
        }
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> getTokenizers() {
        return AccessController.doPrivileged((PrivilegedAction<Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>>>) () -> {
            Map<String, AnalysisModule.AnalysisProvider<TokenizerFactory>> extra = new HashMap<>();

            extra.put("texsmart", TexSmartTokenizerFactory::getTexSmartTokenizerFactory);
            extra.put("texsmart_standard", TexSmartTokenizerFactory::getTexSmartStandardTokenizerFactory);
            extra.put("texsmart_index", TexSmartTokenizerFactory::getTexSmartIndexTokenizerFactory);

            return extra;
        });
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        return AccessController.doPrivileged((PrivilegedAction<Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>>>) () -> {
            Map<String, AnalysisModule.AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> extra = new HashMap<>();

            extra.put("texsmart", TexSmartAnalyzerProvider::getTexSmartAnalyzerProvider);
            extra.put("texsmart_standard", TexSmartAnalyzerProvider::getTexSmartStandardAnalyzerProvider);
            extra.put("texsmart_index", TexSmartAnalyzerProvider::getTexSmartIndexAnalyzerProvider);

            return extra;
        });
    }
}
