package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.persistence.MovieStore;
import java.util.Collection;

@Service
public class MovieService {

    private final MovieStore movieStore;

    public MovieService(MovieStore movieStore) {
        this.movieStore = movieStore;
    }

    public Collection<Movie> findAll() {
        return movieStore.findAll();
    }

    public Movie findById(int id) {
        return movieStore.findById(id);
    }

    public void add(Movie movie) {
        movieStore.add(movie);
    }

    public boolean update(Movie movie) {
        return movieStore.update(movie);
    }
}
