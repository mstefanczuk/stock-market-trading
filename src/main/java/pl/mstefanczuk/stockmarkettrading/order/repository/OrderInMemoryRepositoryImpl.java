package pl.mstefanczuk.stockmarkettrading.order.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.mstefanczuk.stockmarkettrading.order.Order;
import pl.mstefanczuk.stockmarkettrading.order.OrderType;

import java.util.*;

@Repository
@Slf4j
public class OrderInMemoryRepositoryImpl implements OrderInMemoryRepository {

    private final OrderRepository orderDbRepository;
    private final Map<Integer, OrderType> orderTypes = new HashMap<>();
    private final List<Order> orders = new ArrayList<>();

    public OrderInMemoryRepositoryImpl(OrderRepository orderDbRepository) {
        this.orderDbRepository = orderDbRepository;
        OrderType buyType = new OrderType();
        buyType.setId(1);
        buyType.setName("BUY");
        OrderType sellType = new OrderType();
        sellType.setId(2);
        sellType.setName("SELL");
        orderTypes.put(buyType.getId(), buyType);
        orderTypes.put(sellType.getId(), sellType);
    }

    @Override
    public Optional<OrderType> findById(Integer id) {
        return Optional.ofNullable(orderTypes.get(id));
    }

    @Override
    public Order save(Order order) {
        orderDbRepository.save(order).subscribe(o -> {
            orders.add(order);
            log.info("saved. orders size: " + orders.size());
        });
        return order;
    }
}
