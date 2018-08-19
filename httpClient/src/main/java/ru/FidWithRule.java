package ru.nbki;

import java.util.Comparator;

public class FidWithRule {
    final long fid;
    final String rule;

    public long getFid() {
        return fid;
    }

    public String getRule() {
        return rule;
    }

    public FidWithRule(long fid, String rule) {
        this.fid = fid;
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "FidWithRule{" +
                "fid=" + fid +
                ", rule='" + rule + '\'' +
                '}';
    }



}
