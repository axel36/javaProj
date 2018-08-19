package ru.mbtc.bmiservice.service;

import ru.mbtc.bmiservice.entities.OneRecord;

import java.util.ArrayList;
import java.util.List;

public class BMIcalcImpl implements BMIcalc {

    private List<OneRecord> records = new ArrayList<>();

    @Override
    public double calcIndex(Integer heightCm, Integer weightKg) {
        final double height = heightCm / 100.0;
        return (double) weightKg / (height * height);
    }

    @Override
    public String getTextResult(double idx) {
        if (idx < 16) {
            return "Severe Thinness";
        }
        if (idx < 17) {
            return "Moderate Thinness";
        }
        if (idx < 19) {
            return "Mild Thinness";
        }
        if (idx < 25) {
            return "Normal";
        }
        if (idx < 30) {
            return "Overweight";
        }
        if (idx < 35) {
            return "Obese Class I";
        }
        if (idx < 40) {
            return "Obese Class II";
        }
        return "Obese Class III";

    }

    @Override
    public List<OneRecord> getRecords() {
        return records;
    }

    @Override
    public void addOneRecord(OneRecord record) {
        records.add(record);
    }

}
