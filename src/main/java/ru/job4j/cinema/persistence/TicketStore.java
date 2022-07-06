package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class TicketStore {
    private static final Logger LOG = LoggerFactory.getLogger(TicketStore.class.getName());
    private final BasicDataSource pool;

    public TicketStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public void addTicket(Ticket ticket) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO ticket(movie_id, pos_row, cell, user_id) VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, ticket.getMovieId());
            ps.setInt(2, ticket.getPosRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, ticket.getUserId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in addTicket method (TicketStore) : ", e);
        }
    }

    public Collection<Ticket> getAll() {
        List<Ticket> allTickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM ticket")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    allTickets.add(new Ticket(it.getInt("id"),
                            it.getInt("movie_id"),
                            it.getInt("pos_row"),
                            it.getInt("cell"),
                            it.getInt("user_id")
                    ));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in getAll method (TicketStore) : ", e);
        }
        return allTickets;
    }

    public Collection<Ticket> getAllByUser(User user) {
        List<Ticket> allTicketsByUser = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM ticket WHERE user_id = ?")
        ) {
            ps.setInt(1, user.getId());
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    allTicketsByUser.add(new Ticket(it.getInt("id"),
                            it.getInt("movie_id"),
                            it.getInt("pos_row"),
                            it.getInt("cell"),
                            it.getInt("user_id")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in getAllByUser method (TicketStore) : ", e);
        }
        return allTicketsByUser;
    }

    public Collection<Ticket> getByPosRowAndMovieId(int movieId, int posRow) {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM ticket WHERE movie_id = ? AND pos_row = ?")
        ) {
            ps.setInt(1, movieId);
            ps.setInt(2, posRow);
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    tickets.add(new Ticket(it.getInt("id"),
                            it.getInt("movie_id"),
                            it.getInt("pos_row"),
                            it.getInt("cell"),
                            it.getInt("user_id")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in getByPosRowAndMovieId method (TicketStore) : ", e);
        }
        return tickets;
    }
}
