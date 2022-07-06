package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Movie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MovieStore {
    private static final Logger LOG = LoggerFactory.getLogger(MovieStore.class.getName());
    private final BasicDataSource pool;

    public MovieStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM movies")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    movies.add(new Movie(it.getInt("id"),
                            it.getString("name"), it.getTime("time").toLocalTime()));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in findAll method (MovieStore) : ", e);
        }
        return movies;
    }

    public Movie add(Movie movie) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO movies(name, time) VALUES (?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, movie.getName());
            Time time = Time.valueOf(movie.getTime());
            ps.setTime(2, time);
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    movie.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in add method (MovieStore) : ", e);
        }
        return movie;
    }

    public boolean update(Movie movie) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement statement =
                     cn.prepareStatement("UPDATE movies set name = ?, time = ? where id = ?")) {
            statement.setString(1, movie.getName());
            Time time = Time.valueOf(movie.getTime());
            statement.setTime(2, time);
            statement.setInt(3, movie.getId());
            result = statement.executeUpdate() > 0;
        } catch (Exception e) {
            LOG.error("Exception in update method (MovieStore) : ", e);
        }
        return result;
    }

    public Movie findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM movies WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return new Movie(it.getInt("id"), it.getString("name"), it.getTime("time").toLocalTime());
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in findById method (MovieStore) : ", e);
        }
        return null;
    }
}
