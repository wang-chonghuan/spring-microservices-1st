package microservices.book.gamification.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.challenge.ChallengeSolvedEvent;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class GameEventHandler {
    private final GameService gameService;

    void handleMultiplicationSolved(final ChallengeSolvedEvent event) {
        log.info("challenge solved event received: {}", event.getAttemptId());
        try {
            gameService.newAttemptFromUser(event);
        } catch (final Exception e) {
            log.error("error when trying to process challengeSovedEvent", e);
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
