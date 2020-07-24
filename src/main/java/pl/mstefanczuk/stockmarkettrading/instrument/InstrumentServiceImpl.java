package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.stockmarkettrading.message.Message;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final UserInstrumentRepository userInstrumentRepository;
    private final InstrumentPriceRepository instrumentPriceRepository;
    private final InstrumentPriceHistoryRepository instrumentPriceHistoryRepository;
    private final SimpMessagingTemplate template;

    private final Map<Long, BigDecimal> currentPrices = new HashMap<>();

    @Override
    public void setCurrentPrices(Map<Long, BigDecimal> prices) {
        List<InstrumentPrice> instrumentPriceList = new ArrayList<>();
        prices.forEach((k, v) -> {
            if (currentPrices.get(k) == null || v.compareTo(currentPrices.get(k)) != 0) {
                currentPrices.put(k, v);
                InstrumentPrice instrumentPrice = instrumentPriceRepository.save(
                        createInstrumentPrice(instrumentRepository.findById(k).orElse(null), v));
                instrumentPriceHistoryRepository.save(createInstrumentPriceHistory(instrumentPrice));
                instrumentPriceList.add(instrumentPrice);
            }
        });
        sendCurrentPricesOnTopic(instrumentPriceList);
    }

    @Override
    public Iterable<UserInstrument> saveAll(Iterable<UserInstrument> instruments) {
        return userInstrumentRepository.saveAll(instruments);
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
}
