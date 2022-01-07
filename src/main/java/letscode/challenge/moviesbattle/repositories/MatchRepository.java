package letscode.challenge.moviesbattle.repositories;

import letscode.challenge.moviesbattle.entities.Match;
import letscode.challenge.moviesbattle.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findMatchByStatusAndUserUsername(Match.MatchStatus running, String username);

    List<Match> findMatchByUserAndStatus(User user, Match.MatchStatus ended);
}
