package pl.mstefanczuk.stockmarkettrading.instrument;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentPriceRepository extends CrudRepository<InstrumentPrice, Long> {

}
