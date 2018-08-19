package ru.mbtc.bmiservice.services;

import ru.mbtc.bmiservice.entities.OneRecord;

import java.util.List;

public interface BMIcalc {
    double calcIndex(Integer heightCm, Integer weightKg);

    String getTextResult(double idx);

    String getLastTextResult();

    List<OneRecord> getLastRecords();

    void addOneRecord(OneRecord record);

}
