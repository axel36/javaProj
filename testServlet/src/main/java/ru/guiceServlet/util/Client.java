package ru.mbtc.guiceServlet.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Client {
    private final String surname;
    private final String firstname;
    private final String middlename;
    private final LocalDate birthdate;
    private final List<String> phones;

    public String getSurname() {
        return HashTools.hexOf(surname);
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
            hashPhones.add(HashTools.hexOf(number));
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