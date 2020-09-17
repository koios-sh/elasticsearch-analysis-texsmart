package com.texsmart.tokenizer;

import com.texsmart.TexSmart;
import com.texsmart.seg.Segment;
import tencent.ai.texsmart.NluOutput.Term;

import java.util.List;

public class StandardTokenizer {
    public static final Segment SEGMENT = TexSmart.newSegment();

    public StandardTokenizer() {
    }

    public static List<Term> segment(String text) {
        return SEGMENT.seg(text);
    }
}
