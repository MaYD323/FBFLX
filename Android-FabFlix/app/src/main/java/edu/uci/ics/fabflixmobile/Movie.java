package edu.uci.ics.fabflixmobile;

import org.json.JSONArray;

import java.util.ArrayList;

public class Movie {
    private String title;
    private Integer year;
    private String director;
    private ArrayList<String> genre;
    private ArrayList<String> star;



    public Movie(String title, int year, String director, ArrayList genre, ArrayList star) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.genre = genre;
        this.star = star;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }
    public String getDirector() {
        return director;
    }
    public ArrayList<String> getGenre() {
        return genre;
    }
    public ArrayList getStar() {
        return star;
    }
}
