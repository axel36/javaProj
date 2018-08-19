package ru.mbtc.guiceServlet.fid;

import com.google.inject.Inject;
import ru.mbtc.guiceServlet.util.FidWithRule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class FIDUtilsImpl implements FIDUtils {

    private final Logger logger;

    @Inject
    FIDUtilsImpl(Logger logger) {
        this.logger = logger;
    }

    public long findBestFID(List<FidWithRule> fidsWithRules) {

        String ruleFioDr_1 = "TEST.RULE_1";
        String ruleFioDrPhone_1a = "TEST.RULE_1a";
        String ruleNamePhone_2 = "TEST.RULE_2";
        String ruleNamePhoneFam_2a = "TEST.RULE_2a";
        String ruleNamePhoneFamDr_2a_i = "TEST.RULE_2a_i";
        String rulePhone_3 = "TEST.RULE_3";

        if (fidsWithRules.size() < 1) {
            logger.info("no rule matches");
            return -1;
        }
        if (fidsWithRules.size() == 1) {
            logger.info("find rule " + fidsWithRules.get(0).getRule());
            return fidsWithRules.get(0).getFid();
        }
        List<FidWithRule> listWithOneRule = new ArrayList<>(fidsWithRules.size());
        List<FidWithRule> listWithAnotherRule = new ArrayList<>(fidsWithRules.size());
        List<FidWithRule> listWithThirdRule = new ArrayList<>(fidsWithRules.size());
        for (FidWithRule f : fidsWithRules) {
            if (f.getRule().equals(ruleFioDr_1))
                listWithOneRule.add(f);
            if (f.getRule().equals(ruleFioDrPhone_1a))
                listWithAnotherRule.add(f);
        }
        if (listWithOneRule.size() > 0) {
            if (listWithOneRule.size() == 1) {
                logger.info("find rule 1");
                return listWithOneRule.get(0).getFid();
            } else {
                logger.info("find rule 1a");
                return listWithAnotherRule
                        .stream()
                        .max(Comparator.comparing(FidWithRule::getFid))
                        .orElseThrow(NoSuchElementException::new)
                        .getFid();
            }
        }

        listWithAnotherRule.clear();
        listWithOneRule.clear();
        for (FidWithRule f : fidsWithRules) {
            if (f.getRule().equals(ruleNamePhone_2))
                listWithOneRule.add(f);
            if (f.getRule().equals(ruleNamePhoneFam_2a))
                listWithAnotherRule.add(f);
            if (f.getRule().equals(ruleNamePhoneFamDr_2a_i))
                listWithThirdRule.add(f);
        }
        if (listWithOneRule.size() > 0) {
            if (listWithOneRule.size() == 1) {
                logger.info("find rule 2");
                return listWithOneRule.get(0).getFid();
            } else if (listWithAnotherRule.size() == 1) {
                logger.info("find rule 2a");
                return listWithAnotherRule.get(0).getFid();
            } else {
                logger.info("find rule 2a_i");
                return listWithThirdRule
                        .stream()
                        .max(Comparator.comparing(FidWithRule::getFid))
                        .orElseThrow(NoSuchElementException::new)
                        .getFid();
            }
        }

        listWithOneRule.clear();
        for (FidWithRule f : fidsWithRules)
            if (f.getRule().equals(rulePhone_3))
                listWithOneRule.add(f);

        if (listWithOneRule.size() > 0) {
            logger.info("find rule 3");
            return listWithOneRule
                    .stream()
                    .max(Comparator.comparing(FidWithRule::getFid))
                    .orElseThrow(NoSuchElementException::new)
                    .getFid();
        } else {
            return -1;
        }
    }
}
