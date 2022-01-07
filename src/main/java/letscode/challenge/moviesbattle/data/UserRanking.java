package letscode.challenge.moviesbattle.data;

public class UserRanking {

    private Float ranking;
    private String name;

    public Float getRanking() {
        return ranking;
    }

    public String getName() {
        return name;
    }

    public UserRanking(Float ranking, String name) {
        this.ranking = ranking;
        this.name = name;
    }
}
