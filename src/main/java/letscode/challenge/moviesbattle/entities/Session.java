package letscode.challenge.moviesbattle.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Session {

    @Id
    @Column(unique = true, nullable = false)
    private String token;

    @ManyToOne
    private User user;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime expireIn;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(LocalDateTime expireIn) {
        this.expireIn = expireIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
