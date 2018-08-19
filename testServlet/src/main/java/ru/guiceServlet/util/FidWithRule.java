package ru.mbtc.guiceServlet.util;


public class FidWithRule {
    final long fid;
    final String rule;

    public long getFid() {
        return fid;
    }

    public String getRule() {
        return rule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FidWithRule that = (FidWithRule) o;

        return fid == that.fid && rule.equals(that.rule);
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
