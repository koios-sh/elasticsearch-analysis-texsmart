package com.texsmart.seg;

import com.texsmart.TexSmart;
import tencent.ai.texsmart.NluOutput;
import tencent.ai.texsmart.NluOutput.Term;

import java.util.List;

public class TexSmartBasicSegment extends Segment {

    private static String formatOptions = "{" +
            "    \"input_spec\":{\"lang\":\"auto\"}," +
            "    \"word_seg\":{\"enable\":true},\n" +
            "    \"pos_tagging\":{\"enable\":true,\"alg\":\"%s\"}," +
            "    \"ner\":{\"enable\":true,\"alg\":\"%s\",\"fine_grained\":true}," +
            "    \"syntactic_parsing\":{\"enable\":false}," +
            "    \"srl\":{\"enable\":false}" +
            "  }";

    public TexSmartBasicSegment() {
    }

    @Override
    protected List<Term> segSentence(String text) {
        NluOutput output = TexSmart.TEX_ENGINE.parseText(text, String.format(
                formatOptions, config.getPosAlgType(), config.getNerAlgType()));
        if (null == output) return null;
        if (config.isIndexMode()) {
            return output.words();
        } else {
            return output.phrases();
        }
    }
}
