package edu.uci.ics.fabflixmobile;

public class Person {
    private String name;
    private Integer birthYear;

    public Person(String name, int birthYear) {
        this.name = name;
        this.birthYear = birthYear;
    }

    public String getName() {
        return name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }
}
