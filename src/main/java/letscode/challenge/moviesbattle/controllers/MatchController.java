package letscode.challenge.moviesbattle.controllers;

import letscode.challenge.moviesbattle.data.MatchResponse;
import letscode.challenge.moviesbattle.data.QuizResponse;
import letscode.challenge.moviesbattle.entities.Match;
import letscode.challenge.moviesbattle.entities.Quiz;
import letscode.challenge.moviesbattle.entities.User;
import letscode.challenge.moviesbattle.exceptions.InvalidAttributeException;
import letscode.challenge.moviesbattle.exceptions.InvalidMatchActionException;
import letscode.challenge.moviesbattle.services.MatchService;
import letscode.challenge.moviesbattle.services.UserAndSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/match")
public class MatchController {

    private final UserAndSessionService userAndSessionService;
    private final MatchService matchService;

    public MatchController(UserAndSessionService userAndSessionService, MatchService matchService) {
        this.userAndSessionService = userAndSessionService;
        this.matchService = matchService;
    }

    @PostMapping
    public ResponseEntity<MatchResponse> startOrTerminateMatch(@RequestParam String action, @RequestHeader("x-usertoken") String userToken) {
        User user = userAndSessionService.getUserFromToken(userToken);

        if ("start".equalsIgnoreCase(action)) {
            Match match = matchService.startMatch(user);
            return ResponseEntity
                    .created(URI.create("/v1/match/" + match.getId()))
                    .body(new MatchResponse(
                            match.getId(),
                            match.getUser().getName(),
                            match.getStatus().toString(),
                            match.getErrors(),
                            match.getQuizList().size()));
        }

        if ("terminate".equalsIgnoreCase(action)) {
            matchService.endMatch(user);
            return ResponseEntity.noContent().build();
        }

        throw new InvalidMatchActionException("Invalid action. The option are: 'start' or 'terminate'.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponse> getMatchById(@PathVariable Long id) {
        Match match = matchService.findMatchById(id);
        if (match == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MatchResponse(
                match.getId(),
                match.getUser().getName(),
                match.getStatus().toString(),
                match.getErrors(),
                match.getQuizList().size()));
    }

    @GetMapping("/{id}/quiz")
    public ResponseEntity<QuizResponse> getLatestQuizFromMatch(@PathVariable Long id) {
        Quiz quiz = matchService.getLatestQuiz(id);
        return ResponseEntity.ok(
                new QuizResponse(quiz.getMatch().getId(), quiz.getId(), quiz.getMatch().getStatus().toString(), quiz.getMovieLeft().getTitle(), quiz.getMovieRight().getTitle(), quiz.getCorrect()));
    }

    @GetMapping("/{matchId}/quiz/{quizId}")
    public ResponseEntity<QuizResponse> getQuizById(@PathVariable Long matchId, @PathVariable Long quizId) {
        Quiz quiz = matchService.getQuizById(quizId);
        if (quiz == null) {
            throw new InvalidAttributeException("quizId", quizId.toString());
        }
        if (matchId.equals(quiz.getMatch().getId())) {
            throw new InvalidAttributeException("matchId", matchId.toString());
        }
        return ResponseEntity.ok(
                new QuizResponse(quiz.getMatch().getId(), quiz.getId(), quiz.getMatch().getStatus().toString(), quiz.getMovieLeft().getTitle(), quiz.getMovieRight().getTitle(), quiz.getCorrect()));
    }

    @PostMapping("/{id}/quiz")
    public ResponseEntity<QuizResponse> applyChoice(@PathVariable Long id, @RequestParam String choice) {
        Quiz quiz = matchService.getLatestQuiz(id);
        quiz = matchService.applyChoice(quiz, choice);
        return ResponseEntity
                .created(URI.create("/v1/match/" + id))
                .body(new QuizResponse(quiz.getMatch().getId(), quiz.getId(), quiz.getMatch().getStatus().toString(), quiz.getMovieLeft().getTitle(), quiz.getMovieRight().getTitle(), quiz.getCorrect()));
    }
}
