package com.texsmart.seg;

import com.texsmart.TexSmart;
import tencent.ai.texsmart.NluOutput;
import tencent.ai.texsmart.NluOutput.Term;

import java.util.List;

public class TexSmartBasicSegment extends Segment {
    public TexSmartBasicSegment() {
    }

    @Override
    protected List<Term> segSentence(String text) {
        NluOutput output = TexSmart.TEX_ENGINE.parseText(text);
        if (null == output) return null;
        if (config.isIndexMode()) {
            return output.words();
        } else {
            return output.phrases();
        }
    }
}
