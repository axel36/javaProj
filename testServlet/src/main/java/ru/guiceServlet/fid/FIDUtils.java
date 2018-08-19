package ru.mbtc.guiceServlet.fid;

import ru.mbtc.guiceServlet.util.FidWithRule;

import java.util.List;

public interface FIDUtils {
    long findBestFID(List<FidWithRule> fidsWithRules);
}
