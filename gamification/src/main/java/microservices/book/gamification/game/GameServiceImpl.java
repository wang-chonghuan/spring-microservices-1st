package microservices.book.gamification.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.challenge.ChallengeSolvedDTO;
import microservices.book.gamification.game.badgeprocessors.BadgeProcessor;
import microservices.book.gamification.game.domain.BadgeCard;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.ScoreCard;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final ScoreRepository scoreRepository;
    private final BadgeRepository badgeRepository;
    /**
     * todo!key
     * Since we use constructor injection in GameServiceImpl with a list of BadgeProcessor
     * objects, Spring will find all the beans that implement this interface and pass them to us.
     * This is a flexible way of extending our game without interfering with other existing logic.
     * We just need to add new BadgeProcessor implementations and annotate them with @
     * Component so they are loaded in the Spring context.
     *
     * 因为用了RequiredArgsConstructor，所以这个final成员badgeProcessors会自动被一个构造函数构造，
     * 而且该构造函数是@Autowired的，也就是该final成员能被自动注入
     * spring会寻找所有实现了BadgeProcessor接口的@Component注入到这个List里面
     * 这是命令模式，但是是用了自动注入，不需要手动new再add进list了
     *
     */
    private final List<BadgeProcessor> badgeProcessors;


    @Override
    public GameResult newAttemptFromUser(ChallengeSolvedDTO challenge) {
        if(challenge.isCorrect()) {
            ScoreCard scoreCard = new ScoreCard(challenge.getUserId(), challenge.getAttemptId());
            scoreRepository.save(scoreCard);
            log.info("userAlias {}, score {}, attemptId {}",
                    challenge.getUserAlias(), scoreCard.getScore(), challenge.getAttemptId());
            List<BadgeCard> badgeCards = processForBadges(challenge);
            return new GameResult(
                    scoreCard.getScore(),
                    badgeCards
                            .stream()
                            .map(BadgeCard::getBadgeType)
                            .collect(Collectors.toList()));
        } else {
            log.info("attempId {} is not correct, userAlias {} does not get score",
                    challenge.getAttemptId(), challenge.getUserAlias());
            return new GameResult(0, List.of());
        }
    }

    private List<BadgeCard> processForBadges(final ChallengeSolvedDTO solvedChallenge) {
        Optional<Integer> optTotalScore = scoreRepository.getTotalScoreForUser(solvedChallenge.getUserId());
        if(optTotalScore.isEmpty()) {
            return Collections.emptyList();
        }
        int totalScore = optTotalScore.get();

        // get the total score and existing badges for that user
        List<ScoreCard> scoreCardList = scoreRepository
                .findByUserIdOrderByScoreTimestampDesc(solvedChallenge.getUserId());
        // 用stream，去重
        Set<BadgeType> alreadyGotBadges = badgeRepository
                .findByUserIdOrderByBadgeTimestampDesc(solvedChallenge.getUserId())
                .stream()
                .map(BadgeCard::getBadgeType)
                .collect(Collectors.toSet());

        // 调用badge processors生成新的badges
        List<BadgeCard> newBadgeCards = badgeProcessors
                .stream()
                .filter(bp -> !alreadyGotBadges.contains(bp.badgeType()))
                .map(bp -> bp.processForOptionalBadge(totalScore, scoreCardList, solvedChallenge))
                .flatMap(Optional::stream)
                .map(badgeType -> new BadgeCard(solvedChallenge.getUserId(), badgeType))
                .collect(Collectors.toList());
        badgeRepository.saveAll(newBadgeCards);
        return newBadgeCards;
    }
}
