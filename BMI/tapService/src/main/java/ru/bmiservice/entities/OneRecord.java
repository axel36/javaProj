package ru.mbtc.bmiservice.entities;

import ru.mbtc.bmiservice.data.Gender;

import java.util.Date;


public class OneRecord {

    private Integer age;

    private Gender gender;

    private Integer height;

    private Integer weight;

    private Double index;

    private String result;

    private Date date;

    public OneRecord(Integer age, Gender gender, Integer height, Integer weight, Double index, String result) {
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.index = index;
        this.result = result;
        this.date = new Date();
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}