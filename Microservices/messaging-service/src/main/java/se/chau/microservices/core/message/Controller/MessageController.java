package se.chau.microservices.core.message.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import se.chau.microservices.api.core.Message.*;
import se.chau.microservices.core.message.Service.FirebaseService;

import java.util.List;

@RestController
public class MessageController implements MessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    private final FirebaseService firebaseService;

    @Autowired
    public MessageController(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @Override
    public Object text(MessageText messageText) {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            log.info("JWT Subject: " + jwt.getSubject());
            log.info("JWT Claims: " + jwt.getClaims());
            var sender = jwt.getSubject();
            return this.firebaseService.sendMessage(
                    messageText.getRoom(), sender, messageText.getMessage(), messageText.getType());
        } else {
            return "dell xác thực được mày nên mày dell nt đc";
        }

    }

    @Override
    public Mono<Void> creatARoom(RequestCreateRoom roomId) {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            log.info("JWT Subject: " + jwt.getSubject());
            log.info("JWT Claims: " + jwt.getClaims());
            var sender = jwt.getSubject();
            return this.firebaseService.createRoom(roomId.getRoomId(),sender);
        } else {
            return null;
        }
    }

    @Override
    public Object addParticipants(RequestAddParticipants requestAddParticipants) {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            log.info("JWT Subject: " + jwt.getSubject());
            log.info("JWT Claims: " + jwt.getClaims());
            var sender = jwt.getSubject();
            return this.firebaseService.addParticipant(requestAddParticipants.getRoomId(),sender,requestAddParticipants.getUserId());
        } else {
            return null;
        }
    }

    @Override
    public Mono<List<MessageText>> getMessageText(RequestForGetMessage request) {

        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Jwt jwt) {
            log.info("JWT Subject: " + jwt.getSubject());
            log.info("JWT Claims: " + jwt.getClaims());
            var sender = jwt.getSubject();
            return this.firebaseService.getLast20Messages(request.getRoomId(),sender);
        } else {
            return null;
        }
    }

}