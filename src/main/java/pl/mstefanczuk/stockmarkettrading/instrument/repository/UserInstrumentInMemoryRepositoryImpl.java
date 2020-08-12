package pl.mstefanczuk.stockmarkettrading.instrument.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.mstefanczuk.stockmarkettrading.instrument.UserInstrument;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class UserInstrumentInMemoryRepositoryImpl implements UserInstrumentInMemoryRepository {

    private final Map<UserInstrumentKey, UserInstrument> userInstruments = new HashMap<>();

    @Override
    public Optional<UserInstrument> findByUserIdAndInstrumentId(Long userId, Long instrumentId) {
        return Optional.ofNullable(userInstruments.get(new UserInstrumentKey(instrumentId, userId)));
    }

    @Override
    public UserInstrument save(UserInstrument userInstrument) {
        setUserInstrumentInitValues(userInstrument);
        userInstruments.put(new UserInstrumentKey(userInstrument.getInstrument().getId(), userInstrument.getUser().getId()),
                userInstrument);
        return userInstrument;
    }

    @Override
    public List<UserInstrument> saveAll(Iterable<UserInstrument> instruments) {
        List<UserInstrument> savedInstruments = new ArrayList<>();
        instruments.forEach(i -> {
            setUserInstrumentInitValues(i);
            savedInstruments.add(i);
            userInstruments.put(new UserInstrumentKey(i.getInstrument().getId(), i.getUser().getId()), i);
        });
        return savedInstruments;
    }

    private void setUserInstrumentInitValues(UserInstrument instrument) {
        if (instrument.getId() == null) {
            instrument.setId((long) (userInstruments.size() + 1));
        }

        if (instrument.getAmount() == null) {
            instrument.setAmount(BigDecimal.valueOf(1000.00));
        }
        if (instrument.getTradingAmount() == null) {
            instrument.setTradingAmount(BigDecimal.ZERO);
        }
        if (instrument.getBalance() == null) {
            instrument.setBalance(BigDecimal.ZERO);
        }
    }

    @AllArgsConstructor
    private static class UserInstrumentKey {

        private final Long userInstrumentId;
        private final Long userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserInstrumentKey)) return false;
            UserInstrumentKey key = (UserInstrumentKey) o;
            return userInstrumentId.equals(key.userInstrumentId) && userId.equals(key.userId);
        }

        @Override
        public int hashCode() {
            int result = userInstrumentId.intValue();
            result = 31 * result + userId.intValue();
            return result;
        }

    }
}
