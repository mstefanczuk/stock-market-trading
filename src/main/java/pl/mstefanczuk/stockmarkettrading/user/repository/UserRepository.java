package pl.mstefanczuk.stockmarkettrading.user.repository;

import org.springframework.data.repository.CrudRepository;
import pl.mstefanczuk.stockmarkettrading.user.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByLogin(String login);
}
