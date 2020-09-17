package org.elasticsearch.index.analysis;

/**
 * @project: elasticsearch-analysis-texsmart
 * @description: TexSmart分词类型
 * @author: wei_liu
 * @create: 2020-09-09 15:10
 */
public enum TexSmartType {
    /**
     * 默认分词
     */
    TEXSMART,
    /**
     * 中文基础分词
     */
    STANDARD,
    /**
     * 中文单词分词
     */
    SINGLE,
    /**
     * 英文分词
     */
    ENGLISH
}
