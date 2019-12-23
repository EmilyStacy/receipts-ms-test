package com.aa.fly.receipts.domain;

import java.util.Objects;

public class FormOfPaymentKey {
    private String fopSeqId;
    private String fopTypeCode;

    public FormOfPaymentKey() {
    }

    public FormOfPaymentKey(String fopSeqId, String fopTypeCode) {
        this.fopSeqId = fopSeqId;
        this.fopTypeCode = fopTypeCode;
    }

    public String getFopSeqId() {
        return fopSeqId;
    }

    public void setFopSeqId(String fopSeqId) {
        this.fopSeqId = fopSeqId;
    }

    public String getFopTypeCode() {
        return fopTypeCode;
    }

    public void setFopTypeCode(String fopTypeCode) {
        this.fopTypeCode = fopTypeCode;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FormOfPaymentKey that = (FormOfPaymentKey) o;
        return Objects.equals(fopSeqId, that.fopSeqId) &&
                Objects.equals(fopTypeCode, that.fopTypeCode);
    }

    @Override public int hashCode() {
        return Objects.hash(fopSeqId, fopTypeCode);
    }
}
