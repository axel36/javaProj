package ru.mbtc.guiceServlet.fid;

import org.junit.Test;
import ru.mbtc.guiceServlet.util.FidWithRule;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class FIDUtilsImplTest {
    List<FidWithRule> fidWithRules = new ArrayList<>();
    Logger logger = Logger.getLogger( FIDUtilsImplTest.class.getName() );
    FIDUtils fidUtils = new FIDUtilsImpl(logger) ;

    public FIDUtilsImplTest() {
    }

    @Test
    public void findBestFID_RULE1() throws Exception {
        fidWithRules.add(new FidWithRule(15,"RULE_1"));

        long fid = fidUtils.findBestFID(fidWithRules);
        assertEquals(15,fid);
    }
    @Test
    public void findBestFID_RULE1A() throws Exception {
        fidWithRules.add(new FidWithRule(11,"TEST.RULE_1"));
        fidWithRules.add(new FidWithRule(12,"TEST.RULE_1"));
        fidWithRules.add(new FidWithRule(15,"TEST.RULE_1"));
        fidWithRules.add(new FidWithRule(12,"TEST.RULE_1a"));
        fidWithRules.add(new FidWithRule(15,"TEST.RULE_1a"));
        long fid = fidUtils.findBestFID(fidWithRules);
        assertEquals(15,fid);
    }
    @Test
    public void findBestFID_RULE2() throws Exception {
        fidWithRules.add(new FidWithRule(11,"TEST.RULE_2"));
        long fid = fidUtils.findBestFID(fidWithRules);
        assertEquals(11,fid);
    }
    @Test
    public void findBestFID_RULE2A() throws Exception {
        fidWithRules.add(new FidWithRule(11,"TEST.RULE_2"));
        fidWithRules.add(new FidWithRule(10002,"TEST.RULE_2"));
        fidWithRules.add(new FidWithRule(11111,"TEST.RULE_2"));
        fidWithRules.add(new FidWithRule(10002,"TEST.RULE_2a"));
        long fid = fidUtils.findBestFID(fidWithRules);
        assertEquals(10002,fid);
    }
    @Test
    public void findBestFID_RULE2AI() throws Exception {
        fidWithRules.add(new FidWithRule(11,"TEST.RULE_2"));
        fidWithRules.add(new FidWithRule(12,"TEST.RULE_2"));
        fidWithRules.add(new FidWithRule(10002,"TEST.RULE_2"));
        fidWithRules.add(new FidWithRule(11111,"TEST.RULE_2"));
        fidWithRules.add(new FidWithRule(10002,"TEST.RULE_2a"));
        fidWithRules.add(new FidWithRule(11,"TEST.RULE_2a"));
        fidWithRules.add(new FidWithRule(12,"TEST.RULE_2a"));
        fidWithRules.add(new FidWithRule(10002,"TEST.RULE_2a_i"));
        fidWithRules.add(new FidWithRule(11,"TEST.RULE_2a_i"));

        long fid = fidUtils.findBestFID(fidWithRules);
        assertEquals(10002,fid);
    }
    @Test
    public void findBestFID_RULE3() throws Exception {
        fidWithRules.add(new FidWithRule(11,"TEST.RULE_3"));
        fidWithRules.add(new FidWithRule(111,"TEST.RULE_3"));
        fidWithRules.add(new FidWithRule(10,"TEST.RULE_3"));
        long fid = fidUtils.findBestFID(fidWithRules);
        assertEquals(111,fid);
    }
    @Test
    public void findBestFID_NONERULe() throws Exception {
        long fid = fidUtils.findBestFID(fidWithRules);
        assertEquals(-1,fid);
    }

}