package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Movie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class SessionStore {
    private static final Logger LOG = LoggerFactory.getLogger(SessionStore.class.getName());
    private final BasicDataSource pool;

    public SessionStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Collection<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM movies")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    movies.add(new Movie(it.getInt("id"),
                            it.getString("name"),
                            it.getTime("time").toLocalTime()));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in getAll method (SessionStore) : ", e);
        }
        return movies;
    }

    public Optional<Movie> getById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM movies WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return Optional.of(new Movie(it.getInt("id"),
                            it.getString("name"),
                            it.getTime("time").toLocalTime()));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in getById method (SessionStore) : ", e);
        }
        return Optional.empty();
    }
}
