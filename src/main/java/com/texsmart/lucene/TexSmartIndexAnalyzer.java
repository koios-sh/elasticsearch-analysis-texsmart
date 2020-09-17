package com.texsmart.lucene;

import com.texsmart.TexSmart;
import com.texsmart.cfg.Configuration;
import org.apache.lucene.analysis.Analyzer;

public class TexSmartIndexAnalyzer extends Analyzer {
    /**
     * 分词配置
     */
    private Configuration configuration;

    public TexSmartIndexAnalyzer(Configuration configuration) {
        this.configuration = configuration;
        this.configuration.enableIndexMode(true);
    }

    public TexSmartIndexAnalyzer() {
        super();
    }

    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldName) {
        return new Analyzer.TokenStreamComponents(
                TokenizerBuilder.tokenizer(TexSmart.newSegment().enableIndexMode(true), configuration));
    }
}
