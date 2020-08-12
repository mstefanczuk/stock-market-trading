package pl.mstefanczuk.stockmarkettrading.instrument.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.mstefanczuk.stockmarkettrading.instrument.Instrument;

@Repository
public interface InstrumentRepository extends CrudRepository<Instrument, Long> {
}
