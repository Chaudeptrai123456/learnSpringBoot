package se.chau.microservices.core.message.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {
    @Autowired
    private final FirebaseService firebaseService;

    public ChatWebSocketHandler(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        return session.receive()
                .map(msg -> msg.getPayloadAsText())
                .flatMap(payload -> {
                    // Giả sử payload = "roomId|sender|message|type"
                    String[] data = payload.split("\\|");
                    return firebaseService.sendMessage(data[0], data[1], data[2], data[3]);
                })
                .then();
    }
}
