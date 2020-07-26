package pl.mstefanczuk.stockmarkettrading.order;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.mstefanczuk.stockmarkettrading.instrument.InstrumentService;
import pl.mstefanczuk.stockmarkettrading.instrument.UserInstrument;
import pl.mstefanczuk.stockmarkettrading.user.User;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    public static final String STOCK_MARKET_SERVICE_URL = "http://stock-market-service:8080";

    private final OrderRepository orderRepository;
    private final OrderTypeRepository orderTypeRepository;
    private final InstrumentService instrumentService;
    private final RestTemplate restTemplate;
    private final SimpMessagingTemplate template;

    private Map<Long, BigDecimal> lastPrices;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderTypeRepository orderTypeRepository,
                            InstrumentService instrumentService,
                            SimpMessagingTemplate template) {
        this.orderRepository = orderRepository;
        this.orderTypeRepository = orderTypeRepository;
        this.instrumentService = instrumentService;
        this.template = template;
        this.restTemplate = new RestTemplate();
    }

    public void startListening(User user, Principal principal) {
        lastPrices = instrumentService.getCurrentPrices();
        while (true) {
            Map<Long, BigDecimal> currentPrices = instrumentService.getCurrentPrices();
            currentPrices.forEach((k, v) -> handlePriceChange(k, v, user, principal));
        }
    }

    private void handlePriceChange(Long id, BigDecimal price, User user, Principal principal) {
        BigDecimal lastPrice = lastPrices.get(id);
        UserInstrument userInstrument = instrumentService.findUserInstrument(user.getId(), id);
        if (userInstrument == null) {
            return;
        }
        if (BigDecimal.ZERO.compareTo(userInstrument.getAmount()) != 0 && price.compareTo(lastPrice) > 0) {
            Order sellOrder = sell(id, user, price, userInstrument);
            template.convertAndSendToUser(principal.getName(), "/queue/orders", sellOrder);
            template.convertAndSendToUser(principal.getName(),
                    "/queue/instruments", Collections.singletonList(userInstrument));
            lastPrices.put(id, price);
        }
        if (price.compareTo(lastPrice) < 0) {
            Order purchaseOrder = purchase(id, user, price, userInstrument);
            template.convertAndSendToUser(principal.getName(), "/queue/orders", purchaseOrder);
            template.convertAndSendToUser(principal.getName(),
                    "/queue/instruments", Collections.singletonList(userInstrument));
            lastPrices.put(id, price);
        }
    }

    private Order purchase(Long instrumentId, User user, BigDecimal price, UserInstrument userInstrument) {
        Order order = sendRequestToStockMarketService(instrumentId, user, price, Order.Type.BUY, userInstrument);
        userInstrument.setAmount(userInstrument.getAmount().add(userInstrument.getTradingAmount()));
        userInstrument.setBalance(userInstrument.getBalance().subtract(userInstrument.getTradingAmount().multiply(order.getStockServicePrice())));
        instrumentService.save(userInstrument);
        return order;
    }

    private Order sell(Long instrumentId, User user, BigDecimal price, UserInstrument userInstrument) {
        Order order = sendRequestToStockMarketService(instrumentId, user, price, Order.Type.SELL, userInstrument);
        userInstrument.setAmount(userInstrument.getAmount().subtract(userInstrument.getTradingAmount()));
        userInstrument.setBalance(userInstrument.getBalance().add(userInstrument.getTradingAmount().multiply(order.getStockServicePrice())));
        instrumentService.save(userInstrument);
        return order;
    }

    private Order sendRequestToStockMarketService(Long instrumentId,
                                                  User user,
                                                  BigDecimal price,
                                                  Order.Type type,
                                                  UserInstrument userInstrument) {
        OrderDTO dto = new OrderDTO(instrumentId, user.getId(), type.id, userInstrument.getTradingAmount());
        LocalDateTime requestTime = LocalDateTime.now();
        OrderResultDTO resultDto = restTemplate.postForObject(STOCK_MARKET_SERVICE_URL + "/purchase", dto, OrderResultDTO.class);
        Order order = new Order();
        order.setAmount(resultDto.getAmount());
        order.setInstrument(instrumentService.findById(instrumentId));
        order.setLocalPrice(price);
        order.setStockServicePrice(resultDto.getPrice());
        order.setType(orderTypeRepository.findById(type.id).orElse(null));
        order.setUser(user);
        order.setRequestDateTime(requestTime);
        order.setResponseDateTime(resultDto.getDateTime());
        return orderRepository.save(order);
    }
}
