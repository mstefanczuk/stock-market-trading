package pl.mstefanczuk.stockmarketservice.rate;

import java.math.BigDecimal;

public interface InstrumentPriceService {

    BigDecimal getCurrent(Long id);

    void broadcastCurrentPrices();
}
