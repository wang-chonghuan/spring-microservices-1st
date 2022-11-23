package microservices.book.gamification.game;

import lombok.Value;
import microservices.book.gamification.challenge.ChallengeSolvedDTO;
import microservices.book.gamification.game.domain.BadgeType;

import java.util.List;

public interface GameService {
    /**
     * process a new attempt from a given user
     * @param challengeSolved the challenge data with user details, factors, etc.
     * @return a {@link GameResult} object containing the new score and badge cards obtained
     */
    GameResult newAttemptFromUser(ChallengeSolvedDTO challengeSolved);
    @Value
    class GameResult {
        int score;
        List<BadgeType> badges;
    }
}
