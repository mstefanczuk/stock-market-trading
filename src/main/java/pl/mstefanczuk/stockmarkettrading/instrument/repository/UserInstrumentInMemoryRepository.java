package pl.mstefanczuk.stockmarkettrading.instrument.repository;

import pl.mstefanczuk.stockmarkettrading.instrument.UserInstrument;

import java.util.List;
import java.util.Optional;

public interface UserInstrumentInMemoryRepository {

    Optional<UserInstrument> findByUserIdAndInstrumentId(Long userId, Long instrumentId);
    UserInstrument save(UserInstrument userInstrument);
    List<UserInstrument> saveAll(Iterable<UserInstrument> instruments);
}
