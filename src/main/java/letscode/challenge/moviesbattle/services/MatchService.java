package letscode.challenge.moviesbattle.services;

import letscode.challenge.moviesbattle.entities.Match;
import letscode.challenge.moviesbattle.entities.Movie;
import letscode.challenge.moviesbattle.entities.Quiz;
import letscode.challenge.moviesbattle.entities.User;
import letscode.challenge.moviesbattle.exceptions.InvalidAttributeException;
import letscode.challenge.moviesbattle.exceptions.InvalidMatchActionException;
import letscode.challenge.moviesbattle.exceptions.InvalidMatchStatusException;
import letscode.challenge.moviesbattle.repositories.MatchRepository;
import letscode.challenge.moviesbattle.repositories.MovieRepository;
import letscode.challenge.moviesbattle.repositories.QuizRepository;
import letscode.challenge.moviesbattle.repositories.ScoreRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Value("${movies-battle.limit-errors}")
    private Integer limitErrors;

    private final RankingService rankingService;

    private final MatchRepository matchRepository;
    private final QuizRepository quizRepository;
    private final MovieRepository movieRepository;
    private final ScoreRepository scoreRepository;

    public MatchService(MatchRepository matchRepository, QuizRepository quizRepository, MovieRepository movieRepository, ScoreRepository scoreRepository, RankingService rankingService) {
        this.matchRepository = matchRepository;
        this.quizRepository = quizRepository;
        this.movieRepository = movieRepository;
        this.scoreRepository = scoreRepository;
        this.rankingService = rankingService;
    }

    public Match startMatch(User user) {
        if (isMatchRunningToUser(user)) {
            throw new InvalidMatchActionException("Cannot create new match because user has matchs running");
        }

        Match newMatch = new Match();
        newMatch.setUser(user);
        newMatch.setStatus(Match.MatchStatus.RUNNING);
        newMatch.setErrors(0);
        newMatch.setQuizList(new ArrayList<>());
        newMatch = matchRepository.save(newMatch);

        createNewQuiz(newMatch);

        return newMatch;
    }

    private void createNewQuiz(Match match) {
        List<Integer> listMovie = getListMoviesAlreadyUsed(match).stream()
                .collect(Collectors.toCollection(ArrayList::new));

        Quiz newQuiz = new Quiz();
        newQuiz.setCorrect(null);
        newQuiz.setChoice(null);
        newQuiz.setMovieLeft(getRandomMovie(listMovie));
        listMovie.add(newQuiz.getMovieLeft().getId());
        newQuiz.setMovieRight(getRandomMovie(listMovie));
        newQuiz.setMatch(match);
        newQuiz = quizRepository.save(newQuiz);

        match.getQuizList().add(newQuiz);
        matchRepository.save(match);

        populateScoreToMovie(newQuiz.getMovieLeft());
        populateScoreToMovie(newQuiz.getMovieRight());
    }

    private void populateScoreToMovie(Movie movie) {
        if (movie.getScore() == null || movie.getScore() <= 0) {
            Float score = scoreRepository.getScore(movie.getImdbId());
            movie.setScore(score);
            movieRepository.save(movie);
        }
    }

    private List<Integer> getListMoviesAlreadyUsed(Match match) {
        Set<Integer> listMovies = new HashSet<>();
        for (Quiz quiz : match.getQuizList()) {
            listMovies.add(quiz.getMovieLeft().getId());
            listMovies.add(quiz.getMovieRight().getId());
        }
        return listMovies.stream().toList();
    }

    private Movie getRandomMovie(List<Integer> cannotBeUsed) {
        Long numRandom = 0L;
        Random random = new Random();
        long getLastId = movieRepository.count();
        do {
            numRandom = random.nextLong(getLastId) + 1;
        } while (cannotBeUsed.contains(numRandom));

        return movieRepository.getById(numRandom.intValue());
    }

    private boolean isMatchRunningToUser(User user) {
        List<Match> listMatch = matchRepository.findMatchByStatusAndUserUsername(Match.MatchStatus.RUNNING, user.getUsername());
        return listMatch != null && !listMatch.isEmpty();
    }

    public void endMatch(User user) {
        if (!isMatchRunningToUser(user)) {
            throw new InvalidMatchActionException("Cannot end match because all user matchs is stopped");
        }

        List<Match> listRunningMatch = matchRepository.findMatchByUserAndStatus(user, Match.MatchStatus.RUNNING);
        listRunningMatch.stream().forEach(match -> {
            match.setStatus(Match.MatchStatus.ENDED);
            matchRepository.save(match);
        });
        rankingService.updateUserRanking(user);
    }

    public Quiz applyChoice(Quiz quiz, String userChoice) {
        Match match = quiz.getMatch();
        if (Match.MatchStatus.ENDED.equals(match.getStatus())) {
            throw new InvalidMatchStatusException(match.getStatus().toString(), match.getId());
        }

        Quiz.UserChoice userChoiceEnum = mapperChoice(userChoice);
        quiz.setChoice(userChoiceEnum);

        boolean isCorrect = (Quiz.UserChoice.LEFT.equals(userChoiceEnum) && quiz.getMovieLeft().getScore() >= quiz.getMovieRight().getScore())
                || (Quiz.UserChoice.RIGHT.equals(userChoiceEnum) && quiz.getMovieRight().getScore() >= quiz.getMovieLeft().getScore());
        quiz.setCorrect(isCorrect);

        quizRepository.save(quiz);

        if (isCorrect) {
            createNewQuiz(match);
        } else {
            match.setErrors(match.getErrors() + 1);
            if (limitErrors <= match.getErrors()) {
                match.setStatus(Match.MatchStatus.ENDED);
            } else {
                createNewQuiz(match);
            }
        }
        matchRepository.save(match);

        if (limitErrors <= match.getErrors()) {
            rankingService.updateUserRanking(match.getUser());
        }
        return quiz;
    }

    private Quiz.UserChoice mapperChoice(String choice) {
        if ("left".equalsIgnoreCase(choice)) {
            return Quiz.UserChoice.LEFT;
        }
        if ("right".equalsIgnoreCase(choice)) {
            return Quiz.UserChoice.RIGHT;
        }
        throw new InvalidAttributeException("choice", choice);
    }

    public Match findMatchById(Long id) {
        Optional<Match> match = matchRepository.findById(id);
        return match.isPresent() ? match.get() : null;
    }

    public Quiz getLatestQuiz(Long matchId) {
        Optional<Match> match = matchRepository.findById(matchId);
        if (!match.isPresent()) {
            throw new InvalidAttributeException("matchId", matchId.toString());
        }

        if (match.get().getQuizList() == null || match.get().getQuizList().isEmpty()) {
            throw new InvalidAttributeException("quizList", matchId.toString());
        }

        return match.get().getQuizList().get(match.get().getQuizList().size() - 1);
    }

    public Quiz getQuizById(Long quizId) {
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        return (quiz.isPresent() ? quiz.get() : null);
    }
}
