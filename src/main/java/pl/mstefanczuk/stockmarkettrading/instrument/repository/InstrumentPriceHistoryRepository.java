package pl.mstefanczuk.stockmarkettrading.instrument.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mstefanczuk.stockmarkettrading.instrument.InstrumentPriceHistory;

@Repository
public interface InstrumentPriceHistoryRepository extends CrudRepository<InstrumentPriceHistory, Long> {
}
