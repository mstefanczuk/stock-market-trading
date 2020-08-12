package pl.mstefanczuk.stockmarkettrading.order.repository;

import pl.mstefanczuk.stockmarkettrading.order.Order;
import pl.mstefanczuk.stockmarkettrading.order.OrderType;

import java.util.Optional;

public interface OrderInMemoryRepository {

    Optional<OrderType> findById(Integer id);
    Order save(Order order);
}
