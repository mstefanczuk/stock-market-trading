package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import pl.mstefanczuk.stockmarkettrading.message.Message;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class InstrumentController {

    private final InstrumentService instrumentService;

    @MessageMapping("/instrument/save")
    @SendToUser("/queue/reply")
    public Message save(final List<UserInstrument> instruments) {
        instrumentService.saveAll(instruments);
        return Message.builder()
                .body("Zapisano limity. " + getLimits(instruments))
                .time(LocalDateTime.now().toString())
                .build();
    }

    private String getLimits(List<UserInstrument> instruments) {
        return instruments.stream()
                .map(i -> i.getInstrument().getName() + ": " + i.getLimitation())
                .collect(Collectors.joining(", "));
    }
}
