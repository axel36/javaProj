package ru.mbtc.guiceServlet.util;

import com.google.common.base.Strings;
import org.bouncycastle.util.Arrays;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Pattern;


public class NormalizeTools {

    private static final Pattern NOT_DIGITS = Pattern.compile("[^0-9]+");
    private static final Pattern NOT_LETTERS = Pattern.compile("[^А-Я]+"); // в том числе пробелы и их последовательности
    private static final Pattern NOT_A_DOC_NUMBER = Pattern.compile("[^\\dА-ЯA-Z]+");
    private static final Pattern BAD_PHONE = Pattern.compile("0?1234567890?|0?9876543210?|.*(\\d)\\1{5,}.*");

    public final static DateTimeFormatter RU_LOCAL_DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    public static String name(String value) {
        return (value != null ? NOT_LETTERS.matcher(value.toUpperCase().replace('Ё', 'Е')).replaceAll(" ").trim() : null);
    }

    public static String passport(String value) {
        String s = NOT_DIGITS.matcher(value).replaceAll("");
        if (s.length() != 10)
            return null;
        return s;
    }

    public static String docNumber(String value) {
        return (value != null ? NOT_A_DOC_NUMBER.matcher(value.toUpperCase()).replaceAll("") : null);
    }

    public static String phone(String value) {
        String normalized = NOT_DIGITS.matcher(value).replaceAll("");
        if (normalized.length() == 11 && (normalized.startsWith("7") || normalized.startsWith("8"))) {
            normalized = normalized.substring(1);
        }
        if (normalized.length() != 10) {
            return null;
        }
        return normalized;
    }

    /**
     * Вызвращает true для:
     * - Номера, содержащие одну повторяющуюся шесть и более раз подряд цифру, например, 88888888888 или 11000000
     * - Номера, являющиеся последовательностью цифр от 0 или 1 до 9 или 0 и наоборот - 1234567890
     * - Номера горячих линий, начинающиеся с префикса 8800 (800, т.к. +7 или 8 не хранится)
     * - Неполные номера телефонов, длина которых менее 10 символов
     */

    private static int[] VALID_CITY_CODES = {'3','4','8','9'};


    public static boolean badPhone(final String phone) {
        if (Strings.isNullOrEmpty(phone)) {
            return false;
        }
        if (phone.length() < 10) {
            return true;
        }
        if (phone.startsWith("800")) {
            return true;
        }
        if (BAD_PHONE.matcher(phone).matches()) {
            return true;
        }

        // SKIP-38: только корректные коды городов РФ - 3x,4x,8x,9x
        if (phone.length() == 10 && !Arrays.contains(VALID_CITY_CODES,phone.charAt(0))) {
            return true;
        }

        return false;
    }

    public static String phone_dirty(String value) {
        String normalized = NOT_DIGITS.matcher(value).replaceAll("");
        if (normalized.length() == 11 && (normalized.startsWith("7") || normalized.startsWith("8"))) {
            normalized = normalized.substring(1);
        }
        if (normalized.length() < 5) {
            return null;
        }
        return normalized;
    }

    public static String inn(String value) {
        if (value != null && value.length() > 8) {
            final String inn = value.trim();
            if (inn.length() != 10 && inn.length() != 12 && inn.length() != 9)
                return "";
            for (final char c : inn.toCharArray())
                if (!Character.isDigit(c))
                    return "";
            return inn;
        }
        return "";
    }

    public static LocalDate localDate(String value) {
        String v = (value == null) ? "" : value.trim();
        if (v.length() < 10) {
            throw new DateTimeParseException("length of date is less than 10 symbols", v, 0);
        } else if (v.length() > 10) {
            v = v.substring(0, 10);
        }
        try {
            return LocalDate.parse(v, RU_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(v, DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    public static
    @NotNull
    Optional<LocalDate> optionalLocalDate(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(localDate(value));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
