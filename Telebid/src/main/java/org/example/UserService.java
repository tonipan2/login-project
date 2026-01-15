package org.example;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User registerUser(User user) {
        if (userDao.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        user.setPassword(hashPassword(user.getPassword()));

        return userDao.save(user);
    }

    public User updateProfile(User user) {
        return userDao.update(user);
    }

    public String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    public User findByEmail(String email) {
        Optional<User> optionalUser = userDao.findByEmail(email);
        return optionalUser.orElse(null);
    }

    public boolean authenticate(String email, String password) {
        Optional<User> optionalUser = userDao.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();
        return BCrypt.checkpw(password, user.getPassword());
    }
}
