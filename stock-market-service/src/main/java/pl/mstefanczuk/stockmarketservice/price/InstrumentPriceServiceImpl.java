package pl.mstefanczuk.stockmarketservice.price;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class InstrumentPriceServiceImpl implements InstrumentPriceService {

    public static final int CDP_TICK = 400;
    public static final int TESLA_TICK = 600;
    public static final int PGE_TICK = 700;
    public static final int SEND_RATE = 10;

    private final SimpMessagingTemplate template;

    private final Map<Long, Price> currentPrices = new HashMap<>();

    private final Price cdpPrice = new Price(BigDecimal.valueOf(100.00), System.currentTimeMillis());
    private final Price teslaPrice = new Price(BigDecimal.valueOf(200.00), System.currentTimeMillis());
    private final Price pgePrice = new Price(BigDecimal.valueOf(300.00), System.currentTimeMillis());

    private int cdpCounter = 0;
    private int teslaCounter = 0;
    private int pgeCounter = 0;

    private BigDecimal cdpDifference = BigDecimal.ZERO;
    private BigDecimal teslaDifference = BigDecimal.ZERO;
    private BigDecimal pgeDifference = BigDecimal.ZERO;

    public InstrumentPriceServiceImpl(SimpMessagingTemplate template) {
        this.template = template;
        currentPrices.put(1L, cdpPrice);
        currentPrices.put(2L, teslaPrice);
        currentPrices.put(3L, pgePrice);
    }

    @Override
    public Price getCurrent(Long id) {
        return currentPrices.get(id);
    }

    @Override
    @Scheduled(fixedRate = SEND_RATE)
    public void broadcastCurrentPrices() {
        template.convertAndSend("/topic/current-prices", currentPrices);
    }

    @Scheduled(fixedRate = SEND_RATE)
    public void processCdpPriceChanging() {
        BigDecimal price = BigDecimal.valueOf(100.00);
        if (cdpCounter == CDP_TICK) {
            cdpDifference = getRandom();
            price = cdpPrice.getValue().add(cdpDifference);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                price = price.subtract(cdpDifference);
            }
        }

        if (cdpCounter == CDP_TICK + 1) {
            price = cdpPrice.getValue().subtract(cdpDifference);
            cdpCounter = 0;
        }

        cdpPrice.setValue(price);
        cdpPrice.setUpdateTime(System.currentTimeMillis());
        currentPrices.put(1L, cdpPrice);
        cdpCounter++;
    }

    @Scheduled(fixedRate = SEND_RATE * 2)
    public void processTeslaPriceChanging() {
        BigDecimal price = BigDecimal.valueOf(200.00);
        if (teslaCounter == TESLA_TICK) {
            teslaDifference = getRandom();
            price = teslaPrice.getValue().add(teslaDifference);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                price = price.subtract(teslaDifference);
            }
        }

        if (teslaCounter == TESLA_TICK + 1) {
            price = teslaPrice.getValue().subtract(teslaDifference);
            teslaCounter = 0;
        }

        teslaPrice.setValue(price);
        teslaPrice.setUpdateTime(System.currentTimeMillis());
        currentPrices.put(2L, teslaPrice);
        teslaCounter++;
    }

    @Scheduled(fixedRate = SEND_RATE * 3)
    public void processPgePriceChanging() {
        BigDecimal price = BigDecimal.valueOf(300.00);
        if (pgeCounter == PGE_TICK) {
            pgeDifference = getRandom();
            price = pgePrice.getValue().add(pgeDifference);
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                price = price.subtract(pgeDifference);
            }
        }

        if (pgeCounter == PGE_TICK + 1) {
            price = pgePrice.getValue().subtract(pgeDifference);
            pgeCounter = 0;
        }

        pgePrice.setValue(price);
        pgePrice.setUpdateTime(System.currentTimeMillis());
        currentPrices.put(3L, pgePrice);
        pgeCounter++;
    }

    private BigDecimal getRandom() {
        Random random = new Random();
        int sign = random.nextBoolean() ? -1 : 1;
        return BigDecimal.valueOf(Math.random())
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.TEN)
                .multiply(BigDecimal.valueOf(sign));
    }
}
