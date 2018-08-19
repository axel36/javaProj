package ru.mbtc.bmiservice.services;

import ru.mbtc.bmiservice.entities.OneRecord;

import java.util.ArrayList;
import java.util.List;

public class BMIcalcImpl implements BMIcalc{

    private List<OneRecord> records = new ArrayList<>();

    @Override
    public double calcIndex(Integer heightCm, Integer weightKg) {
        final double height = heightCm/100.0;
        return (double)weightKg/ (height*height);
    }
    @Override
    public String getTextResult(double idx) {
        if (idx<16) {
            return "Severe Thinness";
        }
        if (idx<17) {
            return "Moderate Thinness";
        }
        if (idx<19) {
            return "Mild Thinness";
        }
        if (idx<25) {
            return "Normal";
        }
        if (idx<30) {
            return "Overweight";
        }
        if (idx<35) {
            return "Obese Class I";
        }
        if (idx<40) {
            return "Obese Class II";
        }
        return "Obese Class III";

    }
    @Override
    public String getLastTextResult() {
        if (records.size() == 0) {
            return "Check your BMI";
        }
        OneRecord last = records.get(records.size() - 1);
        String strIndex = last.getIndex().toString();
        if(strIndex.length()>6){
            strIndex = strIndex.substring(0, 6);
        }
        return "your BMI is "+strIndex+" it is "+last.getResult();
    }

    @Override
    public List<OneRecord> getLastRecords() {

        if(records.size() == 0){
            return null;
        }
        return records;
    }

    @Override
    public void addOneRecord(OneRecord record) {
        records.add(record);
    }

}
