package com.texsmart;

import com.texsmart.dic.config.TexSmartConfig;
import com.texsmart.help.ESPluginLoggerFactory;
import com.texsmart.seg.Segment;
import com.texsmart.seg.TexSmartBasicSegment;
import com.texsmart.tokenizer.StandardTokenizer;
import org.apache.logging.log4j.Logger;
import tencent.ai.texsmart.NluEngine;
import tencent.ai.texsmart.NluOutput.Term;

import java.util.List;

public class TexSmart {

    private static final Logger logger = ESPluginLoggerFactory.getLogger(TexSmart.class.getName());

    public static NluEngine TEX_ENGINE;

    static {
        TEX_ENGINE = new NluEngine();
        int workerCount = Runtime.getRuntime().availableProcessors();
        logger.info("texsmart analysis is initializing");
        boolean ret = TEX_ENGINE.init(TexSmartConfig.getConfig().getProperty("path"), workerCount);
        if (!ret) {
            logger.info("texsmart analysis load failed");
        } else {
            logger.info("texsmart analysis load success");
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
