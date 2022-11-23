package microservices.book.multiplication.challenge;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * This class provides a REST API to POST the attempts from users.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/attempts")
class ChallengeAttemptController {

    private final ChallengeService challengeService;

    @PostMapping
    ResponseEntity<ChallengeAttempt> postResult(
            @RequestBody @Valid ChallengeAttemptDTO challengeAttemptDTO) {
        return ResponseEntity.ok(challengeService.verifyAttempt(challengeAttemptDTO));
    }

    @GetMapping
    ResponseEntity<List<ChallengeAttempt>> getStatistics(@RequestParam("alias") String alias) {
        return ResponseEntity.ok(
                challengeService.getStatsForUser(alias)
        );
    }

    // 此处必须加一个路径，比如“abandoned”，否则单元测试的时候，该方法和getStatistics会因为endpoints相同而混淆，都是从"/attempts"这里get
    // todo java.lang.IllegalStateException: Failed to load ApplicationContext
    // Caused by: java.lang.IllegalStateException: Ambiguous mapping. Cannot map 'challengeAttemptController' method
    //microservices.book.multiplication.challenge.ChallengeAttemptController#getStatistics(String)
    //to {GET [/attempts]}: There is already 'challengeAttemptController' bean method
    //microservices.book.multiplication.challenge.ChallengeAttemptController#getChallengeWithParam(Long, int) mapped.
    @GetMapping("abandoned")
    public Challenge getChallengeWithParam(
            @PathVariable("challengeId") Long challengeId,
            @RequestParam("factorA") int factorA)
    {
        return null;
    }
}