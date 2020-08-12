package pl.mstefanczuk.stockmarkettrading.instrument.repository;

import org.springframework.data.repository.CrudRepository;
import pl.mstefanczuk.stockmarkettrading.instrument.UserInstrument;

import java.util.Optional;

public interface UserInstrumentRepository extends CrudRepository<UserInstrument, Long> {

    Optional<UserInstrument> findByUserIdAndInstrumentId(Long userId, Long instrumentId);
}
