package pl.mstefanczuk.stockmarketservice.rate;

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

    public static final int CDP_TICK = 4;
    public static final int TESLA_TICK = 6;
    public static final int PGE_TICK = 7;
    public static final int SEND_RATE = 100;

    private final SimpMessagingTemplate template;

    private final Map<Long, BigDecimal> currentPrices = new HashMap<>();

    private BigDecimal cdpPrice = BigDecimal.valueOf(100.00);
    private BigDecimal teslaPrice = BigDecimal.valueOf(200.00);
    private BigDecimal pgePrice = BigDecimal.valueOf(300.00);

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
    public BigDecimal getCurrent(Long id) {
        return currentPrices.get(id);
    }

    @Override
    @Scheduled(fixedRate = SEND_RATE)
    public void broadcastCurrentPrices() {
        template.convertAndSend("/topic/current-prices", currentPrices);
    }

    @Scheduled(fixedRate = SEND_RATE)
    public void processCdpPriceChanging() {
        if (cdpCounter == CDP_TICK) {
            cdpDifference = getRandom();
            cdpPrice = cdpPrice.add(cdpDifference);
            if (cdpPrice.compareTo(BigDecimal.ZERO) < 0) {
                cdpPrice = cdpPrice.subtract(cdpDifference);
            }
            currentPrices.put(1L, cdpPrice);
        }

        if (cdpCounter == CDP_TICK + 1) {
            cdpPrice = cdpPrice.subtract(cdpDifference);
            currentPrices.put(1L, cdpPrice);
            cdpCounter = 0;
        }

        cdpCounter++;
    }

    @Scheduled(fixedRate = SEND_RATE*2)
    public void processTeslaPriceChanging() {
        if (teslaCounter == TESLA_TICK) {
            teslaDifference = getRandom();
            teslaPrice = teslaPrice.add(teslaDifference);
            if (teslaPrice.compareTo(BigDecimal.ZERO) < 0) {
                teslaPrice = teslaPrice.subtract(teslaDifference);
            }
            currentPrices.put(2L, teslaPrice);
        }

        if (teslaCounter == TESLA_TICK + 1) {
            teslaPrice = teslaPrice.subtract(teslaDifference);
            currentPrices.put(2L, teslaPrice);
            teslaCounter = 0;
        }
        teslaCounter++;
    }

    @Scheduled(fixedRate = SEND_RATE*3)
    public void processPgePriceChanging() {
        if (pgeCounter == PGE_TICK) {
            pgeDifference = getRandom();
            pgePrice = pgePrice.add(pgeDifference);
            if (pgePrice.compareTo(BigDecimal.ZERO) < 0) {
                pgePrice = pgePrice.subtract(pgeDifference);
            }
            currentPrices.put(3L, pgePrice);
        }

        if (pgeCounter == PGE_TICK + 1) {
            pgePrice = pgePrice.subtract(pgeDifference);
            currentPrices.put(3L, pgePrice);
            pgeCounter = 0;
        }
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
