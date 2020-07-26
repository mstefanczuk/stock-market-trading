package pl.mstefanczuk.stockmarkettrading.order;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTypeRepository extends CrudRepository<OrderType, Integer> {
}
