package com.texsmart;

import com.texsmart.dic.config.TexSmartConfig;
import com.texsmart.seg.Segment;
import com.texsmart.seg.TexSmartBasicSegment;
import com.texsmart.tokenizer.StandardTokenizer;
import tencent.ai.texsmart.NluEngine;
import tencent.ai.texsmart.NluOutput.Term;

import java.util.List;

public class TexSmart {

    public static NluEngine TEX_ENGINE;

    static {
        TEX_ENGINE = new NluEngine();
        int workerCount = Runtime.getRuntime().availableProcessors();
        boolean ret = TEX_ENGINE.init(TexSmartConfig.getConfig().getProperty("path"), workerCount);
        if (!ret) {
            System.out.println("Failed to initialize the engine");
        }
    }

    private TexSmart() {
    }

    public static List<Term> segment(String text) {
        return StandardTokenizer.segment(text);
    }

    public static Segment newSegment() {
        return new TexSmartBasicSegment();
    }
}
