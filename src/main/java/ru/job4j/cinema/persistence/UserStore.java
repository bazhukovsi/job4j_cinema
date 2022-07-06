package ru.job4j.cinema.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

@Repository
public class UserStore {
    private static final Logger LOG = LoggerFactory.getLogger(UserStore.class.getName());
    private final BasicDataSource pool;

    public UserStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> add(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO users (username, email, password, phone) VALUES (?,?,?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in add method (UserStore) : ", e);
            user = null;
        }
        return Optional.ofNullable(user);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?")
        ) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return Optional.of(new User(it.getInt("id"), it.getString("username"),
                            it.getString("email"), it.getString("password"), it.getString("phone")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in findUserByEmailAndPwd method (UserStore) : ", e);
        }
        return Optional.empty();
    }
}
