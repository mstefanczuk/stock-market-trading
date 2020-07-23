package pl.mstefanczuk.stockmarkettrading.instrument;

import java.math.BigDecimal;
import java.util.Map;

public interface InstrumentService {

    Map<Long, BigDecimal> getCurrentPrices();

    void setCurrentPrices(Map<Long, BigDecimal> prices);

    Iterable<UserInstrument> saveAll(Iterable<UserInstrument> instruments);
}
