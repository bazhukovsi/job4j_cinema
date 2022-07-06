package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.MovieService;
import ru.job4j.cinema.utility.Utility;
import javax.servlet.http.HttpSession;
import java.time.LocalTime;

@Controller
@ThreadSafe
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public String movies(Model model, HttpSession movie) {
        model.addAttribute("movies", movieService.findAll());
        User user = Utility.logUser(movie);
        model.addAttribute("user", user);
        return "movies";
    }

    @GetMapping("/formAddMovie")
    public String addMovie(Model model, HttpSession movie) {
        model.addAttribute("movie", new Movie(0, "Заполните поле",
                LocalTime.of(0, 0)));
        model.addAttribute("movieTime", LocalTime.of(0, 0));
        User user = Utility.logUser(movie);
        model.addAttribute("user", user);
        return "addMovie";
    }

    @PostMapping("/createMovie")
    public String createMovie(@ModelAttribute Movie movie, @RequestParam(name = "time") LocalTime timeTemp) {
        movie.setTime(timeTemp);
        movieService.add(movie);
        return "redirect:/movies";
    }
}
