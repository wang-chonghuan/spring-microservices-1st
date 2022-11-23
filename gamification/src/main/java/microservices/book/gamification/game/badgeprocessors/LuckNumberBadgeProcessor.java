package microservices.book.gamification.game.badgeprocessors;

import microservices.book.gamification.challenge.ChallengeSolvedDTO;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.ScoreCard;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LuckNumberBadgeProcessor implements BadgeProcessor {
    private static final int LUCKY_FACTOR = 42;
    @Override
    public Optional<BadgeType> processForOptionalBadge(int currentScore,
                                                       List<ScoreCard> scoreCardList,
                                                       ChallengeSolvedDTO solvedChallenge) {
        if(solvedChallenge.getFactorA() == LUCKY_FACTOR || solvedChallenge.getFactorB() == LUCKY_FACTOR) {
            return Optional.of(BadgeType.LUCKY_NUMBER);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public BadgeType badgeType() {
        return BadgeType.LUCKY_NUMBER;
    }
}
