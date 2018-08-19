package ru.mbtc.bmiservice.entities;

import ru.mbtc.bmiservice.data.Gender;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class OneRecord {

    private Integer age;

    private Gender gender;

    private Integer height;

    private Integer weight;

    private Double index;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OneRecord oneRecord = (OneRecord) o;

        if (age != null ? !age.equals(oneRecord.age) : oneRecord.age != null) return false;
        if (gender != oneRecord.gender) return false;
        if (height != null ? !height.equals(oneRecord.height) : oneRecord.height != null) return false;
        if (weight != null ? !weight.equals(oneRecord.weight) : oneRecord.weight != null) return false;
        if (index != null ? !index.equals(oneRecord.index) : oneRecord.index != null) return false;
        if (result != null ? !result.equals(oneRecord.result) : oneRecord.result != null) return false;
        return date != null ? date.equals(oneRecord.date) : oneRecord.date == null;
    }

    @Override
    public String toString() {
        return "OneRecord{" +
                "age=" + age +
                ", gender=" + gender +
                ", height=" + height +
                ", weight=" + weight +
                ", index=" + index +
                ", result='" + result + '\'' +
                ", date=" + date +
                '}';
    }

    private String result;

    private Date date;

    public OneRecord() {
    }


    public OneRecord(Integer age, Gender gender, Integer height, Integer weight, Double index, String result) {
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.index = index;
        this.result = result;
        this.date = new Date();
    }

    public OneRecord(Integer age, Gender gender, Integer height, Integer weight, Double index, String result, Date date) {
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.index = index;
        this.result = result;
        this.date = date;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    public Gender getGender() {

        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }


    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }


    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }


    public Double getIndex() {
        return index;
    }

    public void setIndex(Double index) {
        this.index = index;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDate() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        return df.format(date.getTime());
    }

    public void setDate(Date date) {

        this.date = date;
    }


}