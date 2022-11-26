package microservices.book.gamification.game.badgeprocessors;

import microservices.book.gamification.challenge.ChallengeSolvedEvent;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.ScoreCard;

import java.util.List;
import java.util.Optional;

public interface BadgeProcessor {
    /**
     * 判断用户是否拥有一个badge，根据输入的参数
     * @param currentScore
     * @param scoreCardList
     * @param solvedChallenge
     * @return 如果该用户拥有这个badge，就返回这个badge
     */
    Optional<BadgeType> processForOptionalBadge(
            int currentScore,
            List<ScoreCard> scoreCardList,
            ChallengeSolvedEvent solvedChallenge);

    BadgeType badgeType();
}
