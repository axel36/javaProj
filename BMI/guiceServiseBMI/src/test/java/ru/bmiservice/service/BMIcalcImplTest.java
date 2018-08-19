package ru.mbtc.bmiservice.service;

import org.junit.Before;
import org.junit.Test;
import ru.mbtc.bmiservice.data.Gender;
import ru.mbtc.bmiservice.entities.OneRecord;

import static org.junit.Assert.*;

public class BMIcalcImplTest {

    BMIcalcImpl testClass = new BMIcalcImpl();


    @Test
    public void calcIndex() throws Exception {
        int h = 200;
        int w = 40;
        double test = testClass.calcIndex(h, w);
        double result =(w*1.0)/(h*h*0.0001);
        assertEquals(test,result,0.001);

    }

    @Test
    public void getTextResult() throws Exception {
        String[] results = new String[]{"Severe Thinness","Moderate Thinness","Mild Thinness",
                "Normal","Overweight","Obese Class I","Obese Class II","Obese Class III"};
        assertEquals(results[0],testClass.getTextResult(1));
        assertEquals(results[0],testClass.getTextResult(15));
        assertEquals(results[1],testClass.getTextResult(16));
        assertEquals(results[2],testClass.getTextResult(18));
        assertEquals(results[3],testClass.getTextResult(20));
        assertEquals(results[4],testClass.getTextResult(28));
        assertEquals(results[5],testClass.getTextResult(33));
        assertEquals(results[6],testClass.getTextResult(39));
        assertEquals(results[7],testClass.getTextResult(40));
        assertEquals(results[7],testClass.getTextResult(188));
    }

}