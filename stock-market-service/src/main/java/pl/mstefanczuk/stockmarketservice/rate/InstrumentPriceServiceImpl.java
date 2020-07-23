package pl.mstefanczuk.stockmarketservice.rate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstrumentPriceServiceImpl implements InstrumentPriceService {

    private final SimpMessagingTemplate template;

    @Override
    public BigDecimal getCurrent(Long id) {
        return null;
    }

    @Override
    @Scheduled(fixedRate = 100)
    public void broadcastCurrentPrices() {
        template.convertAndSend("/topic/current-prices", getAllCurrent());
    }

    private Map<Long, BigDecimal> getAllCurrent() {
        Map<Long, BigDecimal> map = new HashMap<>();
        map.put(1L, getRandom());
        map.put(2L, getRandom());
        map.put(3L, getRandom());
        return map;
    }

    private BigDecimal getRandom() {
        return BigDecimal.valueOf(Math.random()).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
