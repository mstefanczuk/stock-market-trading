package pl.mstefanczuk.stockmarkettrading.order.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mstefanczuk.stockmarkettrading.order.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
}
