package pl.mstefanczuk.stockmarkettrading.instrument;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentPriceHistoryRepository extends CrudRepository<InstrumentPriceHistory, Long> {
}
