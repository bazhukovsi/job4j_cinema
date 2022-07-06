package ru.job4j.cinema.utility;

import ru.job4j.cinema.model.User;
import javax.servlet.http.HttpSession;

public class Utility {
    public static User logUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setUsername("Гость");
        }
        return user;
    }
}
