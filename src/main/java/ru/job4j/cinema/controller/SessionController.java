package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.MovieService;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.UserService;
import ru.job4j.cinema.utility.Utility;
import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
public class SessionController {
    private final SessionService sessionService;
    private final UserService userService;
    private final MovieService movieService;

    public SessionController(SessionService sessionService, UserService userService, MovieService movieService) {
        this.sessionService = sessionService;
        this.userService = userService;
        this.movieService = movieService;
    }

    private User getSessionUser(HttpSession session) {
        return Utility.logUser(session);
    }

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        model.addAttribute("user", getSessionUser(session));
        model.addAttribute("movies", sessionService.getAll());
        model.addAttribute("ticket", new Ticket());
        return "index";
    }

    @PostMapping("/takePosRow")
    public String takeRowPost(Model model, @ModelAttribute Ticket ticket,
                              @RequestParam(name = "movie.id") int id, HttpSession session) {
        ticket.setMovieId(id);
        session.setAttribute("ticket", ticket);
        session.setAttribute("movieId", id);
        return "redirect:/takePosRow";
    }

    @GetMapping("/takePosRow")
    public String takeRowGet(Model model, HttpSession session) {
        model.addAttribute("movie", movieService.findById((Integer) session.getAttribute("movieId")));
        model.addAttribute("user", getSessionUser(session));
        model.addAttribute("rows", sessionService.rows());
        return "takePosRow";
    }

    @PostMapping("/takeCell")
    public String takeCellPost(@ModelAttribute Ticket ticket, HttpSession session) {
        Ticket sessionTicket = (Ticket) session.getAttribute("ticket");
        sessionTicket.setPosRow(ticket.getPosRow());
        return "redirect:/takeCell";
    }

    @GetMapping("/takeCell")
    public String takeCellGet(Model model, HttpSession session) {
        Ticket sessionTicket = (Ticket) session.getAttribute("ticket");
        model.addAttribute("movie", movieService.findById((Integer) session.getAttribute("movieId")));
        model.addAttribute("user", getSessionUser(session));
        model.addAttribute("cells", sessionService.freeCells(sessionTicket.getMovieId(), sessionTicket.getPosRow()));
        return "takeCell";
    }

    @PostMapping("/tickets")
    public String ticketsPost(@ModelAttribute Ticket ticket, HttpSession session) {
        Ticket sessionTicket = (Ticket) session.getAttribute("ticket");
        sessionTicket.setCell(ticket.getCell());
        sessionTicket.setUserId(getSessionUser(session).getId());
        sessionService.addTicket(sessionTicket);
        return "redirect:/tickets";
    }

    @GetMapping("/tickets")
    public String ticketsGet(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", getSessionUser(session));
        model.addAttribute("tickets", userService.getUserTickets(user));
        return "tickets";
    }

    public MovieService getMovieService() {
        return movieService;
    }
}
