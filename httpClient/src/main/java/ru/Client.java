package ru.nbki;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private final String surname;
    private final String firstname;
    private final String middlename;
    private final LocalDate birthdate;
    private final List<String> phones;


    public String getSurname() {
        return HashTools.hexOf(NormalizeTools.name(surname));
    }

    public String getFirstname() {
        return HashTools.hexOf(NormalizeTools.name(firstname));
    }

    public String getMiddlename() {
        return HashTools.hexOf(NormalizeTools.name(middlename));
    }

    public String getBirthdate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.uuuu");
        String strBDate = birthdate.format(formatter);
        return strBDate;
    }

    public List<String> getPhones() throws NullPointerException{
        List<String> hashPhones = new ArrayList<>();
        for (String number : phones){
            System.out.println(number);
            hashPhones.add(HashTools.hexOf(NormalizeTools.phone(number)));
        }
        return hashPhones;
    }

    public Client(String surname, String firstname, String middlename, LocalDate birthdate, List<String> phones) {
        this.surname = surname;
        this.firstname = firstname;
        this.middlename = middlename;
        this.birthdate = birthdate;
        this.phones = phones;
    }

}
