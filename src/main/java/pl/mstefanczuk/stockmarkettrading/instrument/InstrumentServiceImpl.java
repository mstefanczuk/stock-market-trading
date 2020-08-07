package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.stockmarkettrading.websocket.Message;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final UserInstrumentRepository userInstrumentRepository;
    private final InstrumentPriceRepository instrumentPriceRepository;
    private final InstrumentPriceHistoryRepository instrumentPriceHistoryRepository;
    private final SimpMessagingTemplate template;

    @Override
    public Map<Long, Price> getCurrentPrices() {
        return StreamSupport.stream(instrumentPriceRepository.findAll().spliterator(), false)
                .map(p -> new AbstractMap.SimpleEntry<>(p.getId(), new Price(p.getPrice(), p.getLastUpdateTime())))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }

    @Override
    public void setCurrentPrices(Map<Long, Price> prices) {
        AtomicBoolean isChange = new AtomicBoolean(false);
        Map<Long, Price> currentPrices = getCurrentPrices();
        List<InstrumentPrice> instrumentPriceList = new ArrayList<>();
        prices.forEach((k, v) -> {
            Price price = new Price(v.getValue(), LocalDateTime.now());
            InstrumentPrice instrumentPrice =
                    createInstrumentPrice(instrumentRepository.findById(k).orElse(null), price, v.getUpdateTime());
            instrumentPriceList.add(instrumentPrice);
            if (currentPrices.get(k) == null || v.getValue().compareTo(currentPrices.get(k).getValue()) != 0) {
                instrumentPriceRepository.save(instrumentPrice);
                instrumentPriceHistoryRepository.save(createInstrumentPriceHistory(instrumentPrice));
                isChange.set(true);
            }
        });
        if (isChange.get()) {
            sendCurrentPricesOnTopic(instrumentPriceList);
        }
    }

    @Override
    public Iterable<UserInstrument> saveAll(Iterable<UserInstrument> instruments) {
        instruments.forEach(i -> {
            setUserInstrumentNullValues(i);
            userInstrumentRepository.findByUserIdAndInstrumentId(i.getUser().getId(), i.getInstrument().getId())
                    .ifPresent(instrument -> i.setId(instrument.getId()));
        });
        return userInstrumentRepository.saveAll(instruments);
    }

    @Override
    public UserInstrument save(UserInstrument userInstrument) {
        setUserInstrumentNullValues(userInstrument);
        return userInstrumentRepository.save(userInstrument);
    }

    @Override
    public UserInstrument findUserInstrument(Long userId, Long instrumentId) {
        return userInstrumentRepository.findByUserIdAndInstrumentId(userId, instrumentId).orElse(null);
    }

    @Override
    public Instrument findById(Long id) {
        return instrumentRepository.findById(id).orElse(null);
    }

    private InstrumentPrice createInstrumentPrice(Instrument instrument, Price localPrice, LocalDateTime stockUpdateTime) {
        InstrumentPrice instrumentPrice = new InstrumentPrice();
        instrumentPrice.setId(instrument.getId());
        instrumentPrice.setInstrument(instrument);
        instrumentPrice.setPrice(localPrice.getValue());
        instrumentPrice.setLastUpdateTime(localPrice.getUpdateTime());
        instrumentPrice.setStockServiceLastUpdateTime(stockUpdateTime);
        return instrumentPrice;
    }

    private InstrumentPriceHistory createInstrumentPriceHistory(InstrumentPrice instrumentPrice) {
        InstrumentPriceHistory instrumentPriceHistory = new InstrumentPriceHistory();
        instrumentPriceHistory.setInstrument(instrumentPrice.getInstrument());
        instrumentPriceHistory.setPrice(instrumentPrice.getPrice());
        instrumentPriceHistory.setStockServiceUpdateTime(instrumentPrice.getStockServiceLastUpdateTime());
        instrumentPriceHistory.setUpdateTime(instrumentPrice.getLastUpdateTime());
        return instrumentPriceHistory;
    }

    private void sendCurrentPricesOnTopic(List<InstrumentPrice> instrumentPriceList) {
        template.convertAndSend("/topic/current-prices",
                Message.builder()
                        .body(instrumentPriceList.stream()
                                .map(p -> p.getInstrument().getName() + ": " + p.getPrice().toString())
                                .collect(Collectors.joining(", ")))
                        .time(LocalDateTime.now().toString())
                        .build());
    }

    private void setUserInstrumentNullValues(UserInstrument instrument) {
        if (instrument.getAmount() == null) {
            instrument.setAmount(BigDecimal.valueOf(1000.00));
        }
        if (instrument.getTradingAmount() == null) {
            instrument.setTradingAmount(BigDecimal.ZERO);
        }
        if (instrument.getBalance() == null) {
            instrument.setBalance(BigDecimal.ZERO);
        }
    }
}
