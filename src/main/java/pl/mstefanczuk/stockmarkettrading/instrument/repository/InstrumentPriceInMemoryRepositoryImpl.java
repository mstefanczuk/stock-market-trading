package pl.mstefanczuk.stockmarkettrading.instrument.repository;

import org.springframework.stereotype.Repository;
import pl.mstefanczuk.stockmarkettrading.instrument.InstrumentPrice;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InstrumentPriceInMemoryRepositoryImpl implements InstrumentPriceInMemoryRepository {

    private final Map<Long, InstrumentPrice> instrumentPrices = new HashMap<>();

    @Override
    public Map<Long, InstrumentPrice> findAll() {
        return instrumentPrices;
    }

    @Override
    public InstrumentPrice save(InstrumentPrice instrumentPrice) {
        if (instrumentPrice.getId() == null) {
            instrumentPrice.setId((long) instrumentPrices.size() + 1);
        }
        instrumentPrices.put(instrumentPrice.getId(), instrumentPrice);
        return instrumentPrice;
    }
}
