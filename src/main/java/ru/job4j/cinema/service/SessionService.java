package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.persistence.SessionStore;
import ru.job4j.cinema.persistence.TicketStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SessionService {
    private final SessionStore sessionStore;
    private final TicketStore ticketStore;
    private static final int ROWS = 3;
    private static final int CELLS = 3;

    public SessionService(SessionStore sessionStore,  TicketStore ticketStore) {
        this.sessionStore = sessionStore;
        this.ticketStore = ticketStore;
    }

    public Collection<Movie> getAll() {
        return sessionStore.getAll();
    }

    public void addTicket(Ticket ticket) {
        ticketStore.addTicket(ticket);
    }

    public List<Integer> rows() {
        List<Integer> rows = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            rows.add(i + 1);
        }
        return rows;
    }

    public List<Integer> cells() {
        List<Integer> cells = new ArrayList<>();
        for (int i = 0; i < CELLS; i++) {
            cells.add(i + 1);
        }
        return cells;
    }

    private Collection<Ticket> getByPosRowAndMovieId(int movieId, int posRow) {
        return ticketStore.getByPosRowAndMovieId(movieId, posRow);
    }

    public Collection<Integer> freeCells(int movieId, int posRow) {
        List<Integer> cells = cells();
        getByPosRowAndMovieId(movieId, posRow).forEach(x -> {
            if (cells.contains(x.getCell())) {
                cells.remove(Integer.valueOf(x.getCell()));
            }
        });
        return cells;
    }
}
