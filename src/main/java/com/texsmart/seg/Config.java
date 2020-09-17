package com.texsmart.seg;

import org.elasticsearch.index.analysis.NerAlgType;
import org.elasticsearch.index.analysis.PosAlgType;

public class Config {
    public int indexMode = 0;
    public boolean useCustomDictionary = true;
    public boolean forceEntName = true;
    public boolean ner = true;
    public boolean offset = false;
    public boolean enableStopDictionary = false;
    public PosAlgType posAlgType = PosAlgType.LOG_LINEAR;
    public NerAlgType nerAlgType = NerAlgType.CRF;

    public Config() {
    }

    public boolean isIndexMode() { return this.indexMode > 0; }

    public String getPosAlgType() {
        return this.posAlgType.getAlg();
    }

    public String getNerAlgType() {
        return this.nerAlgType.getAlg();
    }
}