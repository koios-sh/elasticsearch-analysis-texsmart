package com.texsmart.lucene;

import com.texsmart.dic.stopword.FilterStopWord;
import com.texsmart.seg.Segment;
import com.texsmart.utility.TextUtility;
import tencent.ai.texsmart.NluOutput.Term;
import com.texsmart.cfg.Configuration;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.BufferedReader;
import java.io.IOException;


public class TexSmartTokenizer extends Tokenizer {
    /**
     * 当前词
     */
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    /**
     * 偏移量
     */
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    /**
     * 距离
     */
    private final PositionIncrementAttribute positionAttr = addAttribute(PositionIncrementAttribute.class);
    /**
     * 词性
     */
    private TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    /**
     * 配置
     */
    private Configuration configuration;
    /**
     * 分词器
     */
    private SegmentWrapper segment;
    /**
     *
     */
    private final PorterStemmer stemmer = new PorterStemmer();
    /**
     * 单文档当前所在的总offset，当reset（切换multi-value fields中的value）的时候不清零，在end（切换field）时清零
     */
    private int totalOffset = 0;

    /**
     * @param segment       TexSmart中的某个分词器
     * @param configuration 分词配置
     */
    public TexSmartTokenizer(Segment segment, Configuration configuration) {
        this.configuration = configuration;
        this.segment = new SegmentWrapper(this.input, segment, configuration);
    }

    @Override
    final public boolean incrementToken() throws IOException {
        clearAttributes();
        int position = 0;
        Term term;
        boolean unIncreased = true;
        do {
            term = segment.next();
            if (term == null) {
                totalOffset += segment.offset;
                return false;
            }
            if (TextUtility.isBlank(term.str)) {
                totalOffset += term.length();
                continue;
            }
            if (configuration.isEnablePorterStemming() && term.tag.equals("nx")) {
                term.str = stemmer.stem(term.str);
            }

            final Term copyTerm = term;
            if (!this.configuration.isEnableStopDictionary() || !FilterStopWord.beRemove(copyTerm)) {
                position++;
                unIncreased = false;
            } else {
                totalOffset += term.length();
            }
        }
        while (unIncreased);

        positionAttr.setPositionIncrement(position);
        termAtt.setEmpty().append(term.str);
        offsetAtt.setOffset(correctOffset(term.offset), correctOffset(term.offset + term.str.length()));
        typeAtt.setType(term.tag == null ? "null" : term.tag.toString());
        totalOffset += term.length();
        return true;
    }

    @Override
    public void end() throws IOException {
        super.end();
        offsetAtt.setOffset(totalOffset, totalOffset);
        totalOffset = 0;
    }

    /**
     * 必须重载的方法，否则在批量索引文件时将会导致文件索引失败
     */
    @Override
    public void reset() throws IOException {
        super.reset();
        segment.reset(new BufferedReader(this.input));
    }
}
