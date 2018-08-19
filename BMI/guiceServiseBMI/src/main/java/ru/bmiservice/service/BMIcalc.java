package ru.mbtc.bmiservice.service;

import ru.mbtc.bmiservice.entities.OneRecord;

import java.util.List;

public interface BMIcalc {
    double calcIndex(Integer heightCm, Integer weightKg);

    String getTextResult(double idx);

    List<OneRecord> getRecords();

    void addOneRecord(OneRecord record);

}
