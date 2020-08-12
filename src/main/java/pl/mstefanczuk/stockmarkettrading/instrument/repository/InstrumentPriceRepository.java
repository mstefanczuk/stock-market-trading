package pl.mstefanczuk.stockmarkettrading.instrument.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mstefanczuk.stockmarkettrading.instrument.InstrumentPrice;

@Repository
public interface InstrumentPriceRepository extends CrudRepository<InstrumentPrice, Long> {

}
