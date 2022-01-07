package letscode.challenge.moviesbattle.data;

import java.net.URI;

public class QuizResponse {

    private String matchStatus;
    private String movieTitleLeft;
    private String movieTitleRight;
    private Boolean isCorrect;

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

    public QuizResponse(String matchStatus, String movieTitleLeft, String movieTitleRight, Boolean isCorrect) {
        this.matchStatus = matchStatus;
        this.movieTitleLeft = movieTitleLeft;
        this.movieTitleRight = movieTitleRight;
        this.isCorrect = isCorrect;
    }
}

