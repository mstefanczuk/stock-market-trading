package pl.mstefanczuk.stockmarkettrading.instrument;

import org.springframework.data.repository.CrudRepository;

public interface UserInstrumentRepository extends CrudRepository<UserInstrument, Long> {
}
