package pl.mstefanczuk.stockmarkettrading.order.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mstefanczuk.stockmarkettrading.order.OrderType;

@Repository
public interface OrderTypeRepository extends CrudRepository<OrderType, Integer> {
}
