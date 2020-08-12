package pl.mstefanczuk.stockmarkettrading.instrument.repository;

import pl.mstefanczuk.stockmarkettrading.instrument.InstrumentPriceHistory;

public interface InstrumentPriceHistoryInMemoryRepository {

    InstrumentPriceHistory save(InstrumentPriceHistory instrumentPrice);
}
