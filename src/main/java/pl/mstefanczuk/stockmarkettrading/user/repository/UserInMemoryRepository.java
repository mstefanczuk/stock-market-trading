package pl.mstefanczuk.stockmarkettrading.user.repository;

import pl.mstefanczuk.stockmarkettrading.user.User;

import java.util.Optional;

public interface UserInMemoryRepository {

    Optional<User> findByLogin(String login);
    User save(User user);
}
