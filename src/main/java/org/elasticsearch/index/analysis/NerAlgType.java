package org.elasticsearch.index.analysis;

public enum NerAlgType {
    CRF("crf"), DNN("dnn");

    private String alg;

    NerAlgType(String alg) {
        this.alg = alg;
    }

    public String getAlg() {
        return alg;
    }
}
