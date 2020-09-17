package com.texsmart.lucene;

import com.texsmart.TexSmart;
import com.texsmart.cfg.Configuration;
import org.apache.lucene.analysis.Analyzer;

/**
 * @project: elasticsearch-analysis-texsmart
 * @description: 默认分词
 * @author: wei_liu
 * @create: 2020-09-09 15:10
 */
public class TexSmartAnalyzer extends Analyzer {
    /**
     * 分词配置
     */
    private Configuration configuration;

    public TexSmartAnalyzer(Configuration configuration) {
        this.configuration = configuration;
    }

    public TexSmartAnalyzer() {
        super();
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
        return new Analyzer.TokenStreamComponents(
                TokenizerBuilder.tokenizer(TexSmart.newSegment(), configuration));
    }
}
