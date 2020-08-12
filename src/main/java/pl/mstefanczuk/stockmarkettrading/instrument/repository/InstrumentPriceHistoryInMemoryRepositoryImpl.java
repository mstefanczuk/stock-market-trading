package pl.mstefanczuk.stockmarkettrading.instrument.repository;

import org.springframework.stereotype.Repository;
import pl.mstefanczuk.stockmarkettrading.instrument.InstrumentPriceHistory;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InstrumentPriceHistoryInMemoryRepositoryImpl implements InstrumentPriceHistoryInMemoryRepository {

    private final List<InstrumentPriceHistory> instrumentPriceHistoryList = new ArrayList<>();

    @Override
    public InstrumentPriceHistory save(InstrumentPriceHistory instrumentPrice) {
        if (instrumentPrice.getId() == null) {
            instrumentPrice.setId((long) instrumentPriceHistoryList.size() + 1);
        }
        instrumentPriceHistoryList.add(instrumentPrice);
        return instrumentPrice;
    }
}
