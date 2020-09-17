package com.texsmart.dic.stopword;

import tencent.ai.texsmart.NluOutput.Term;

public interface Filter {
    boolean beRemove(Term var1);
}
