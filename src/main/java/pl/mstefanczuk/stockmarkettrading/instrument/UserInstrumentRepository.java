package pl.mstefanczuk.stockmarkettrading.instrument;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserInstrumentRepository extends CrudRepository<UserInstrument, Long> {

    Optional<UserInstrument> findByUserIdAndInstrumentId(Long userId, Long instrumentId);
}
