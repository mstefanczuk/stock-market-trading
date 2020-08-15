package pl.mstefanczuk.stockmarkettrading.user.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pl.mstefanczuk.stockmarkettrading.user.User;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {
}
