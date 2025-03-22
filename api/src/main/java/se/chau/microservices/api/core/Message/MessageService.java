package se.chau.microservices.api.core.Message;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MessageService {
    @PostMapping(
            value="/message",
            produces =  "application/json",
            consumes = "application/json"
    )
    public Object text(@RequestBody MessageText messageText);
    @PostMapping(
            value="/createRoom",
            produces =  "application/json",
            consumes = "application/json"
    )
    public Object creatARoom(@RequestBody RequestCreateRoom roomId);
    @PostMapping(
            value="/addParticipants",
            produces =  "application/json",
            consumes = "application/json"
    )
    public Object addParticipants(@RequestBody RequestAddParticipants requestAddParticipants);
    @PostMapping(
            value="/getMessage",
            produces =  "application/json",
            consumes = "application/json"
    )
    public Mono<List<MessageText>> getMessageText(@RequestBody RequestForGetMessage request);
}
