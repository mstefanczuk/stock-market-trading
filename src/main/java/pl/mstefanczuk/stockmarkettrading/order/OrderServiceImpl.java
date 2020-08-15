package pl.mstefanczuk.stockmarkettrading.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.stockmarkettrading.instrument.InstrumentPrice;
import pl.mstefanczuk.stockmarkettrading.instrument.InstrumentService;
import pl.mstefanczuk.stockmarkettrading.instrument.UserInstrument;
import pl.mstefanczuk.stockmarkettrading.order.repository.OrderInMemoryRepository;
import pl.mstefanczuk.stockmarkettrading.user.User;
import pl.mstefanczuk.stockmarkettrading.webclient.StockMarketServiceWebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderInMemoryRepository orderRepository;
    private final InstrumentService instrumentService;
    private final StockMarketServiceWebClient webClient;

    private long startTime;

    public OrderServiceImpl(OrderInMemoryRepository orderRepository,
                            InstrumentService instrumentService,
                            StockMarketServiceWebClient webClient) {
        this.orderRepository = orderRepository;
        this.instrumentService = instrumentService;
        this.webClient = webClient;
    }

    public void subscribe(User user) {
        instrumentService.getPrices().subscribe(pair -> {
            startTime = System.nanoTime();
            pair.getFirst().forEach((k, v) -> handlePriceChange(k, v, pair.getSecond().get(k), user));
        });
    }

    private void handlePriceChange(Long id, InstrumentPrice previousPrice, InstrumentPrice currentPrice, User user) {
        UserInstrument userInstrument = instrumentService.findUserInstrument(user.getId(), id);
        if (userInstrument == null) {
            return;
        }
        if (BigDecimal.ZERO.compareTo(userInstrument.getAmount()) != 0
                && currentPrice.getPrice().compareTo(previousPrice.getPrice()) > 0) {
            sell(id, user, currentPrice, userInstrument);
        }
        if (currentPrice.getPrice().compareTo(previousPrice.getPrice()) < 0) {
            purchase(id, user, currentPrice, userInstrument);
        }
    }

    private void purchase(Long instrumentId, User user, InstrumentPrice price, UserInstrument userInstrument) {
        sendRequestToStockMarketService(instrumentId, user, price, Order.Type.BUY, userInstrument);
    }

    private void sell(Long instrumentId, User user, InstrumentPrice price, UserInstrument userInstrument) {
        sendRequestToStockMarketService(instrumentId, user, price, Order.Type.SELL, userInstrument);
    }

    private void sendRequestToStockMarketService(Long instrumentId,
                                                  User user,
                                                  InstrumentPrice price,
                                                  Order.Type type,
                                                  UserInstrument userInstrument) {
        OrderDTO dto = new OrderDTO(instrumentId, user.getId(), type.id, userInstrument.getTradingAmount());
        LocalDateTime requestTime = LocalDateTime.now();
        webClient.order(dto).subscribe(resultDto -> {
            Order order = new Order();
            order.setAmount(resultDto.getAmount());
            order.setInstrumentId(instrumentService.findById(instrumentId).getId());
            order.setLocalPrice(price.getPrice());
            order.setLocalPriceUpdateTime(price.getLastUpdateTime());
            order.setStockServicePriceUpdateTime(price.getStockServiceLastUpdateTime());
            order.setRealStockServicePrice(resultDto.getPrice());
            order.setRealStockServicePriceUpdateTime(resultDto.getPriceUpdateTime());
            order.setTypeId(orderRepository.findById(type.id).map(OrderType::getId).orElse(null));
            order.setUserId(user.getId());
            order.setRequestDateTime(requestTime);
            order.setResponseDateTime(resultDto.getDateTime());
            orderRepository.save(order);

            userInstrument.setAmount(userInstrument.getAmount().subtract(userInstrument.getTradingAmount()));
            userInstrument.setBalance(userInstrument.getBalance().add(userInstrument.getTradingAmount().multiply(order.getRealStockServicePrice())));
            instrumentService.save(userInstrument);
        });
        logTime();
    }

    private void logTime() {
        long endTime = System.nanoTime();
        log.info("Total execution time: " + (endTime - startTime) + "ns");
    }
}
