package org.elasticsearch.index.analysis;

public enum PosAlgType {
    CRF("crf"), DNN("dnn"), LOG_LINEAR("log_LINEAR");

    private String alg;

    PosAlgType(String alg) {
        this.alg = alg;
    }

    public String getAlg() {
        return alg;
    }
}
