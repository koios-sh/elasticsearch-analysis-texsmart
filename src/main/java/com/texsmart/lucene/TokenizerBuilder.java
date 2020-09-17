package com.texsmart.lucene;

import com.texsmart.cfg.Configuration;
import com.texsmart.seg.Segment;
import org.apache.lucene.analysis.Tokenizer;

/**
 * @project: elasticsearch-analysis-texsmart
 * @description:
 * @author: wei_liu
 * @create: 20202-09-09 09:47
 */
public class TokenizerBuilder {

    /**
     * 构建Tokenizer
     *
     * @param segment       原始segment
     * @param configuration 配置信息
     * @return 返回tokenizer
     */
    public static Tokenizer tokenizer(Segment segment, Configuration configuration) {
        Segment seg = segment(segment, configuration);
        return new TexSmartTokenizer(seg, configuration);
    }

    /**
     * 根据配置信息配置segment
     *
     * @param segment       原始segment
     * @param configuration 配置信息
     * @return 新segment
     */
    private static Segment segment(Segment segment, Configuration configuration) {
        if (!configuration.isEnableCustomConfig()) {
            return segment.enableOffset(true);
        }
        segment.enableIndexMode(configuration.isEnableIndexMode())
            .enableOffset(configuration.isEnableOffset())
            .enableStopDictionary(configuration.isEnableStopDictionary());
        return segment;
    }
}
