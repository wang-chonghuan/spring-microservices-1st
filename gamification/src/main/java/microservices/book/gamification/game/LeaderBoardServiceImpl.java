package microservices.book.gamification.game;

import lombok.RequiredArgsConstructor;
import microservices.book.gamification.game.domain.LeaderBoardRow;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderBoardServiceImpl implements LeaderBoardService {

    private final ScoreRepository scoreRepository;
    private final BadgeRepository badgeRepository;

    @Override
    public List<LeaderBoardRow> getCurrentLeaderBoard() {
        // get score only
        List<LeaderBoardRow> scoreOnly = scoreRepository.findFirst10();
        // 和badge组合起来
        return scoreOnly.stream().map(
                row -> { // 每一个row是一个LeaderBoardRow，把badge的名字放进这个row里
                    List<String> badges = badgeRepository
                            .findByUserIdOrderByBadgeTimestampDesc(row.getUserId())
                            .stream()
                            .map(b -> b.getBadgeType().getDescription())
                            .collect(Collectors.toList());
                    return row.withBadges(badges);
                }
        ).collect(Collectors.toList());
    }
}
