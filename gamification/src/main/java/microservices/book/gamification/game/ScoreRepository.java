package microservices.book.gamification.game;

import microservices.book.gamification.game.domain.LeaderBoardRow;
import microservices.book.gamification.game.domain.ScoreCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends CrudRepository<ScoreCard, Long> {
    List<ScoreCard> findByUserIdOrderByScoreTimestampDesc(final Long userId);

    /**
     * select出的userId有多个score，所以要用sum把它们都加起来
     * @param userId
     * @return
     */
    @Query("SELECT SUM(s.score) FROM ScoreCard s WHERE s.userId = :userId GROUP BY s.userId")
    Optional<Integer> getTotalScoreForUser(@Param("userId") Long userId);

    /**
     * 返回10个LeaderBoardRow，分数从大到小排序
     * select出的userId有多个score，所以要用sum把它们都加起来
     * @return
     */
    // todo?
    @Query("SELECT NEW microservices.book.gamification.game.domain.LeaderBoardRow(s.userId, SUM(s.score)) " +
            "FROM ScoreCard s GROUP BY s.userId ORDER BY SUM(s.score) DESC")
    List<LeaderBoardRow> findFirst10();

}
