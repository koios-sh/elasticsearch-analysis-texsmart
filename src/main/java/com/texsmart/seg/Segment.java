package com.texsmart.seg;

import tencent.ai.texsmart.NluOutput.Term;

import java.util.List;

public abstract class Segment {
    protected Config config = new Config();

    public Segment() {
    }

    public List<Term> seg(String text) {
        return segSentence(text);
    }

    protected abstract List<Term> segSentence(String text);

    public Segment enableOffset(boolean enable) {
        this.config.offset = enable;
        return this;
    }

    public Segment enableIndexMode(boolean enable) {
        this.config.indexMode = enable ? 2 : 0;
        return this;
    }

    public Segment enableIndexMode(int minimalLength) {
        if (minimalLength < 1) {
            throw new IllegalArgumentException("最小长度应当大于等于1");
        } else {
            this.config.indexMode = minimalLength;
            return this;
        }
    }

    public Segment enableStopDictionary(boolean enable) {
        this.config.enableStopDictionary = enable;
        return this;
    }
}