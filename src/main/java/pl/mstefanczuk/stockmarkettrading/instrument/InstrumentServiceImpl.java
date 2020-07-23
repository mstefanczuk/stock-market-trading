package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pl.mstefanczuk.stockmarkettrading.message.Message;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InstrumentServiceImpl implements InstrumentService {

    private final UserInstrumentRepository userInstrumentRepository;
    private final SimpMessagingTemplate template;
    private Map<Long, BigDecimal> currentPrices = new HashMap<>();

    @Override
    public Map<Long, BigDecimal> getCurrentPrices() {
        return currentPrices;
    }

    @Override
    public void setCurrentPrices(Map<Long, BigDecimal> prices) {
        currentPrices = prices;
        template.convertAndSend("/topic/current-prices",
                Message.builder()
                        .body(prices.toString())
                        .time(LocalDateTime.now().toString())
                        .build());
    }

    @Override
    public Iterable<UserInstrument> saveAll(Iterable<UserInstrument> instruments) {
        return userInstrumentRepository.saveAll(instruments);
    }
}
