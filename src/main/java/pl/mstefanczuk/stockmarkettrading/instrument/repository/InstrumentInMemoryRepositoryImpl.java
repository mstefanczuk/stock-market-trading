package pl.mstefanczuk.stockmarkettrading.instrument.repository;

import org.springframework.stereotype.Repository;
import pl.mstefanczuk.stockmarkettrading.instrument.Instrument;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InstrumentInMemoryRepositoryImpl implements InstrumentInMemoryRepository {

    private final Map<Long, Instrument> instruments = new HashMap<>();

    public InstrumentInMemoryRepositoryImpl() {
        Instrument cdp = new Instrument();
        cdp.setId(1L);
        cdp.setName("CDPROJEKT");
        Instrument tesla = new Instrument();
        tesla.setId(2L);
        tesla.setName("TESLA");
        Instrument pge = new Instrument();
        pge.setId(3L);
        pge.setName("PGE");
        instruments.put(cdp.getId(), cdp);
        instruments.put(tesla.getId(), tesla);
        instruments.put(pge.getId(), pge);
    }

    @Override
    public Optional<Instrument> findById(Long id) {
        return Optional.ofNullable(instruments.get(id));
    }
}
