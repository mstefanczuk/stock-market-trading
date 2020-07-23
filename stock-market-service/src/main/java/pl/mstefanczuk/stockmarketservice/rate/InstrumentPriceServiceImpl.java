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
    @Scheduled(fixedRate = 1000)
    public void broadcastCurrentPrices() {
        template.convertAndSend("/topic/current-prices", getAllCurrent());
    }

    private Map<Long, BigDecimal> getAllCurrent() {
        Map<Long, BigDecimal> map = new HashMap<>();
        map.put(1L, BigDecimal.valueOf(10.00));
        map.put(2L, BigDecimal.valueOf(20.00));
        map.put(3L, BigDecimal.valueOf(30.00));
        return map;
    }
}
