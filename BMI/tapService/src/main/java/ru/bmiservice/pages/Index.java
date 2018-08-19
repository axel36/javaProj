package ru.mbtc.bmiservice.pages;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import ru.mbtc.bmiservice.data.Gender;
import ru.mbtc.bmiservice.entities.OneRecord;
import ru.mbtc.bmiservice.services.BMIcalc;

import java.util.List;

public class Index {

    @Inject
    BMIcalc bmIcalc;

    @Property
    private Integer age;

    @Property
    private Gender gender;
    @Property
    private Integer heightCm;
    @Property
    private Integer weightKg;


    @Property
    private List<OneRecord> cols;

    @InjectPage
    private Index reloadPage;

    @InjectComponent("features")
    private Form form;

    @InjectComponent("heightCm")
    private TextField heightField;
    @InjectComponent("weightKg")
    private TextField weightField;

    public String getRes() {

        return bmIcalc.getLastTextResult();
    }

    Object onSuccess() {

        final double idx = bmIcalc.calcIndex(heightCm, weightKg);

        bmIcalc.addOneRecord(new OneRecord(age, gender, heightCm, weightKg, idx, bmIcalc.getTextResult(idx)));

        return reloadPage;
    }

    void onValidateFromFeatures() {

        if (heightCm != null && heightCm <= 0) {
            form.recordError(heightField, "Height must be more than 0");
        }
        if( heightCm==null){
            form.recordError(heightField, "Height must not be null");
        }
        if (weightKg == null) {
            form.recordError(weightField, "Weight must not be null");
        }
    }

    void setupRender() {
        cols = bmIcalc.getLastRecords();
     }
}
