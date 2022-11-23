package microservices.book.gamification.game;

import microservices.book.gamification.game.domain.LeaderBoardRow;

import java.util.List;

public interface LeaderBoardService {
    /**
     * @return 返回当前排名，根据score从高到低
     */
    List<LeaderBoardRow> getCurrentLeaderBoard();
}
