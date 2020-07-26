package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pl.mstefanczuk.stockmarkettrading.order.OrderService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequiredArgsConstructor
public class InstrumentController {

    private final InstrumentService instrumentService;
    private final OrderService orderService;

    @MessageMapping("/instrument/save")
    @SendToUser("/queue/instruments")
    public List<UserInstrument> save(final List<UserInstrument> instruments, Principal principal) {
        List<UserInstrument> savedInstruments = StreamSupport.stream(instrumentService.saveAll(instruments).spliterator(), false)
                .collect(Collectors.toList());
        new Thread(() -> orderService.startListening(savedInstruments.get(0).getUser(), principal)).start();
        return savedInstruments;
    }
}
