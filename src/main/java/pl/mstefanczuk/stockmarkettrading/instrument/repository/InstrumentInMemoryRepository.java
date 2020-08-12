package pl.mstefanczuk.stockmarkettrading.instrument.repository;

import pl.mstefanczuk.stockmarkettrading.instrument.Instrument;

import java.util.Optional;

public interface InstrumentInMemoryRepository {

    Optional<Instrument> findById(Long id);
}
