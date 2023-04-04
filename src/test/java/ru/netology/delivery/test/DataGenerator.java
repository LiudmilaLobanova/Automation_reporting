package ru.netology.delivery.test;

import com.github.javafaker.Faker;

import lombok.Value;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;


public class DataGenerator {
    private DataGenerator() {
    }


    public static String generateDate(int shift) {
        return LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    }

    public static String generateCity() {
        String[] cities = {"Москва", "Санкт-Петербург", "Севастополь", "Новосибирск", "Омск", "Ростов-на-Дону", "Саратов", "Смоленск", "Тамбов", "Тверь"};
        return cities[new Random().nextInt(cities.length)];
    }

    public static String generateName(String locale) {
        var faker = new Faker(new Locale(locale));
        String name = faker.name().fullName();
        return name;
    }

    public static String generateWrongCity(String locale) {
        var faker = new Faker(new Locale(locale));
        String wrcity = faker.name().fullName();
        return wrcity;
    }

    public static String generatePhone(String locale) {
        var faker = new Faker(new Locale(locale));
        String phone = faker.numerify("+79#########");
        return phone;
    }

    public static String generateShortPhone(String locale) {
        var faker = new Faker(new Locale(locale));
        String phone = faker.numerify("+79########");
        return phone;
    }

    public static String generateLongPhone(String locale) {
        var faker = new Faker(new Locale(locale));
        String phone = faker.numerify("+79##########");
        return phone;
    }

    public static String generateSpecialCharacter() {
        String[] spchar = {"!", "@", "#", "%", "^", "&", "*", "(", ")", "/", ":", ";"};
        return spchar[new Random().nextInt(spchar.length)];
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            return new UserInfo(generateCity(), generateName(locale), generatePhone(locale));

        }
    }

}

