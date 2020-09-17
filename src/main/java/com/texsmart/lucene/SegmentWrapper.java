package com.texsmart.lucene;

import com.texsmart.cfg.Configuration;
import com.texsmart.seg.Segment;
import tencent.ai.texsmart.NluOutput.Term;

import java.io.Reader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;
import java.util.Scanner;

public class SegmentWrapper {

    private Scanner scanner;

    private Segment segment;
    /**
     * 因为next是单个term出去的，所以在这里做一个记录
     */
    private Term[] termArray;
    /**
     * termArray下标
     */
    private int index;
    /**
     * term的偏移量，由于wrapper是按行读取的，必须对term.offset做一个校正
     */
    int offset;

    Configuration configuration;

    public SegmentWrapper(Reader reader, Segment segment, Configuration configuration) {
        scanner = createScanner(reader);
        this.segment = segment;
        this.configuration = configuration;
    }

    public SegmentWrapper(Reader reader, Segment segment) {
        scanner = createScanner(reader);
        this.segment = segment;
    }

    /**
     * 重置分词器
     *
     * @param reader
     */
    public void reset(Reader reader) {
        scanner = createScanner(reader);
        termArray = null;
        index = 0;
        offset = 0;
    }

    public Term next() {
        if (termArray != null && index < termArray.length) {
            return termArray[index++];
        }
        if (!scanner.hasNextLine()) {
            return null;
        }
        String line = scanner.nextLine();
        while (isBlank(line)) {
            offset += line.length() + 1;
            if (scanner.hasNextLine()) {
                line = scanner.nextLine();
            } else {
                return null;
            }
        }

        final String lineNeedSeg = line;
        List<Term> termList = AccessController.doPrivileged((PrivilegedAction<List<Term>>)() -> {
//            char[] text = lineNeedSeg.toCharArray();
            if (configuration != null && configuration.isEnableNormalization()) {
//                AccessController.doPrivileged((PrivilegedAction) () -> {
//                    CharTable.normalization(text);
//                    return null;
//                });
            }
            return segment.seg(lineNeedSeg);
        });

        if (termList.size() == 0) {
            return null;
        }
        termArray = termList.toArray(new Term[0]);

        for (Term term: termArray) {
            term.offset = term.offset + offset;
        }
        if (scanner.hasNextLine()) {
            offset += line.length() + 1;
        } else {
            offset += line.length();
        }
        index = 0;
        return termArray[index++];
    }

    /**
     * 判断字符串是否为空（null和空格）
     *
     * @param cs
     * @return
     */
    private static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static Scanner createScanner(Reader reader) {
        return new Scanner(reader).useDelimiter("\n");
    }
}

