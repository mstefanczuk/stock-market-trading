package pl.mstefanczuk.stockmarkettrading.instrument.repository;

import pl.mstefanczuk.stockmarkettrading.instrument.InstrumentPrice;

import java.util.Map;

public interface InstrumentPriceInMemoryRepository {

    Map<Long, InstrumentPrice> findAll();
    InstrumentPrice save(InstrumentPrice instrumentPrice);
}
