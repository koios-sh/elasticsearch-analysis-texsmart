package com.texsmart.seg;

public class Config {
    public int indexMode = 0;
    public boolean useCustomDictionary = true;
    public boolean forceEntName = true;
    public boolean ner = true;
    public boolean offset = false;
    public boolean enableStopDictionary = false;

    public Config() {
    }

    public boolean isIndexMode() { return this.indexMode > 0; }
}