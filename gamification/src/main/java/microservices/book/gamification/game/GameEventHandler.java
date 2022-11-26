package microservices.book.gamification.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.challenge.ChallengeSolvedEvent;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class GameEventHandler {
    private final GameService gameService;

    // todo! 如果这里不注解，即使有mq的配置，也不会启动mq，也不会产生连接！！！！！！
    @RabbitListener(queues = "${amqp.queue.gamification}")
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
