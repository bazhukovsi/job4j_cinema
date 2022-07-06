package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.persistence.SessionStore;
import ru.job4j.cinema.persistence.TicketStore;
import ru.job4j.cinema.persistence.UserStore;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService {
    private final TicketStore ticketStore;
    private final UserStore userStore;
    private final SessionStore sessionStore;

    public UserService(TicketStore ticketStore, UserStore userStore, SessionStore sessionStore) {
        this.ticketStore = ticketStore;
        this.userStore = userStore;
        this.sessionStore = sessionStore;
    }

    public Optional<User> add(User user) {
        return userStore.add(user);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return userStore.findUserByEmailAndPwd(email, password);
    }

    public HashMap<Ticket, Movie> getUserTickets(User user) {
        HashMap<Ticket, Movie> map = new HashMap<>();
        ticketStore.getAllByUser(user).forEach(ticket -> map.put(ticket, sessionStore.getById(ticket.getMovieId()).get()));
        return map;
    }
}
