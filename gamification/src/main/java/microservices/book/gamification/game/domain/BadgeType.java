package microservices.book.gamification.game.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BadgeType {
    // badges depending on score
    BRONZE("Bronze"),
    SILVER("Silver"),
    GOLD("Gold"),
    // other badges won for different conditions
    FIRST_WON("First time"),
    LUCKY_NUMBER("Lucky number");
    private final String description;
}
