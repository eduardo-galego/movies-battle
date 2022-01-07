package letscode.challenge.moviesbattle.data;

public class MatchResponse {

    private Long matchId;
    private String user;
    private String status;
    private Integer errors;
    private Integer rounds;

    public MatchResponse(Long matchId, String user, String status, Integer errors, Integer rounds) {
        this.matchId = matchId;
        this.user = user;
        this.status = status;
        this.errors = errors;
        this.rounds = rounds;
    }

    public Long getMatchId() {
        return matchId;
    }

    public String getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }

    public Integer getErrors() {
        return errors;
    }

    public Integer getRounds() {
        return rounds;
    }
}
