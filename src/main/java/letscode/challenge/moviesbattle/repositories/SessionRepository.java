package letscode.challenge.moviesbattle.repositories;

import letscode.challenge.moviesbattle.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Session findFirstByToken(String token);

    Session findFirstByUserUsername(String username);

    @Query("select s from Session s where s.expireIn <= :now")
    List<Session> findExpiredSessions(@Param("now") LocalDateTime now);

}
