package ru.job4j.cinema.model;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalTime;
import java.util.Objects;

public class Movie {
    private int id;
    private String name;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    public Movie(int id, String name, LocalTime time) {
        this.id = id;
        this.name = name;
        this.time = time;
    }

    public Movie(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return true;
        }
        Movie movie = (Movie) o;
        return id == movie.id && Objects.equals(name, movie.name) && Objects.equals(time, movie.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, time);
    }

    @Override
    public String toString() {
        return "Movie{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", movieTime=" + time
                + '}';
    }
}
