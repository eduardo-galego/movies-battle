package letscode.challenge.moviesbattle.entities;

import javax.persistence.*;

@Entity
public class Quiz {

    public enum UserChoice {LEFT, RIGHT}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Movie movieLeft;

    @ManyToOne
    private Movie movieRight;

    private UserChoice choice;
    private Boolean isCorrect;

    @ManyToOne
    private Match match;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovieLeft() {
        return movieLeft;
    }

    public void setMovieLeft(Movie movieLeft) {
        this.movieLeft = movieLeft;
    }

    public Movie getMovieRight() {
        return movieRight;
    }

    public void setMovieRight(Movie movieRight) {
        this.movieRight = movieRight;
    }

    public UserChoice getChoice() {
        return choice;
    }

    public void setChoice(UserChoice choice) {
        this.choice = choice;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
