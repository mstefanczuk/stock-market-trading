package pl.mstefanczuk.stockmarkettrading.instrument;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.stockmarkettrading.instrument.repository.InstrumentInMemoryRepository;
import pl.mstefanczuk.stockmarkettrading.instrument.repository.InstrumentPriceHistoryInMemoryRepository;
import pl.mstefanczuk.stockmarkettrading.instrument.repository.InstrumentPriceInMemoryRepository;
import pl.mstefanczuk.stockmarkettrading.instrument.repository.UserInstrumentInMemoryRepository;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentInMemoryRepository instrumentRepository;
    private final UserInstrumentInMemoryRepository userInstrumentRepository;
    private final InstrumentPriceInMemoryRepository instrumentPriceRepository;
    private final InstrumentPriceHistoryInMemoryRepository instrumentPriceHistoryRepository;

    private final DirectProcessor<Pair<Map<Long, InstrumentPrice>, Map<Long, InstrumentPrice>>> processor;
    private final FluxSink<Pair<Map<Long, InstrumentPrice>, Map<Long, InstrumentPrice>>> sink;

    public InstrumentServiceImpl(InstrumentInMemoryRepository instrumentRepository,
                                 UserInstrumentInMemoryRepository userInstrumentRepository,
                                 InstrumentPriceInMemoryRepository instrumentPriceRepository,
                                 InstrumentPriceHistoryInMemoryRepository instrumentPriceHistoryRepository) {
        this.instrumentRepository = instrumentRepository;
        this.userInstrumentRepository = userInstrumentRepository;
        this.instrumentPriceRepository = instrumentPriceRepository;
        this.instrumentPriceHistoryRepository = instrumentPriceHistoryRepository;
        this.processor = DirectProcessor.create();
        this.sink = processor.sink();
    }

    @Override
    public Flux<Pair<Map<Long, InstrumentPrice>, Map<Long, InstrumentPrice>>> getPrices() {
        return Flux.concat(
                Flux.just(Pair.of(instrumentPriceRepository.findAll(), instrumentPriceRepository.findAll())),
                processor.map(pair -> pair)
        );
    }

    @Override
    public void setCurrentPrices(Map<Long, Price> prices) {
        AtomicBoolean isChange = new AtomicBoolean(false);
        Map<Long, InstrumentPrice> currentPrices = instrumentPriceRepository.findAll();
        Map<Long, InstrumentPrice> previousPrices = new HashMap<>(currentPrices);
        List<InstrumentPrice> instrumentPriceList = new ArrayList<>();
        prices.forEach((k, v) -> {
            Price price = new Price(v.getValue(), LocalDateTime.now());
            InstrumentPrice instrumentPrice =
                    createInstrumentPrice(instrumentRepository.findById(k).orElse(null), price, v.getUpdateTime());
            instrumentPriceList.add(instrumentPrice);
            if (currentPrices.get(k) == null || v.getValue().compareTo(currentPrices.get(k).getPrice()) != 0) {
                instrumentPriceRepository.save(instrumentPrice);
                instrumentPriceHistoryRepository.save(createInstrumentPriceHistory(instrumentPrice));
                isChange.set(true);
            }
        });
        if (isChange.get()) {
            emitPrices(previousPrices);
            sendCurrentPricesOnTopic(instrumentPriceList);
        }
    }

    @Override
    public List<UserInstrument> saveAll(Iterable<UserInstrument> instruments) {
        return userInstrumentRepository.saveAll(instruments);
    }

    @Override
    public UserInstrument save(UserInstrument userInstrument) {
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

    private void emitPrices(Map<Long, InstrumentPrice> previousPrices) {
        Map<Long, InstrumentPrice> currentPrices = new HashMap<>(instrumentPriceRepository.findAll());
        sink.next(Pair.of(previousPrices, currentPrices));
    }

    private void sendCurrentPricesOnTopic(List<InstrumentPrice> instrumentPriceList) {
//        template.convertAndSend("/topic/current-prices",
//                Message.builder()
//                        .body(instrumentPriceList.stream()
//                                .map(p -> p.getInstrument().getName() + ": " + p.getPrice().toString())
//                                .collect(Collectors.joining(", ")))
//                        .time(LocalDateTime.now().toString())
//                        .build());
    }
}
