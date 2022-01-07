package letscode.challenge.moviesbattle.data;

public class QuizResponse {

    private final Long matchId;
    private final Long quizId;
    private final String matchStatus;
    private final String movieTitleLeft;
    private final String movieTitleRight;
    private final Boolean isCorrect;

    public Long getMatchId() {
        return matchId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public String getMovieTitleLeft() {
        return movieTitleLeft;
    }

    public String getMovieTitleRight() {
        return movieTitleRight;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public QuizResponse(Long matchId, Long quizId, String matchStatus, String movieTitleLeft, String movieTitleRight, Boolean isCorrect) {
        this.matchId = matchId;
        this.quizId = quizId;
        this.matchStatus = matchStatus;
        this.movieTitleLeft = movieTitleLeft;
        this.movieTitleRight = movieTitleRight;
        this.isCorrect = isCorrect;
    }
}

