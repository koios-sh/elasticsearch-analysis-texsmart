package com.texsmart.dic.stopword;

import tencent.ai.texsmart.NluOutput.Term;

public class FilterStopWord {
    private static FilterStopWord ourInstance = new FilterStopWord();

    private static Filter FILTER = term -> {
        // 除掉停用词 (目前只去掉标点符号)
        String nature = term.tag != null ? term.tag : "空";
        return nature.equals("PU");
    };

    public static FilterStopWord getInstance() {
        return ourInstance;
    }

    private FilterStopWord() {}

    public static boolean beRemove(Term term) {
        return FILTER.beRemove(term);
    }
}
