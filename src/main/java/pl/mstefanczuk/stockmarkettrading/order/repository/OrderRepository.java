package pl.mstefanczuk.stockmarkettrading.order.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pl.mstefanczuk.stockmarkettrading.order.Order;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
}
