package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.stockmarkettrading.websocket.Message;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public Map<Long, BigDecimal> getCurrentPrices() {
        return StreamSupport.stream(instrumentPriceRepository.findAll().spliterator(), false)
                .collect(Collectors.toMap(InstrumentPrice::getId, InstrumentPrice::getPrice));
    }

    @Override
    public void setCurrentPrices(Map<Long, BigDecimal> prices) {
        AtomicBoolean isChange = new AtomicBoolean(false);
        Map<Long, BigDecimal> currentPrices = getCurrentPrices();
        List<InstrumentPrice> instrumentPriceList = new ArrayList<>();
        prices.forEach((k, v) -> {
            InstrumentPrice instrumentPrice = createInstrumentPrice(instrumentRepository.findById(k).orElse(null), v);
            instrumentPriceList.add(instrumentPrice);
            if (currentPrices.get(k) == null || v.compareTo(currentPrices.get(k)) != 0) {
                currentPrices.put(k, v);
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

    private InstrumentPrice createInstrumentPrice(Instrument instrument, BigDecimal price) {
        InstrumentPrice instrumentPrice = new InstrumentPrice();
        instrumentPrice.setId(instrument.getId());
        instrumentPrice.setInstrument(instrument);
        instrumentPrice.setPrice(price);
        instrumentPrice.setLastUpdateTime(LocalDateTime.now());
        return instrumentPrice;
    }

    private InstrumentPriceHistory createInstrumentPriceHistory(InstrumentPrice instrumentPrice) {
        InstrumentPriceHistory instrumentPriceHistory = new InstrumentPriceHistory();
        instrumentPriceHistory.setInstrument(instrumentPrice.getInstrument());
        instrumentPriceHistory.setPrice(instrumentPrice.getPrice());
        instrumentPriceHistory.setDateTime(instrumentPrice.getLastUpdateTime());
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
