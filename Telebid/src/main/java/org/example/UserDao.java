package org.example;
import java.util.Optional;

public interface UserDao {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    User save(User user);

    User update(User user);
}

