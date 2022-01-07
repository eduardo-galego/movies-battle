package letscode.challenge.moviesbattle.services;

import com.google.common.hash.Hashing;
import letscode.challenge.moviesbattle.entities.Session;
import letscode.challenge.moviesbattle.entities.User;
import letscode.challenge.moviesbattle.exceptions.InvalidSessionException;
import letscode.challenge.moviesbattle.repositories.SessionRepository;
import letscode.challenge.moviesbattle.repositories.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class UserAndSessionService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public UserAndSessionService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean isValidSession(String userToken) {
        Session session = sessionRepository.findFirstByToken(userToken);
        return session != null;
    }

    public Session createOrRetrieveSession(String username, String password) {
        User user = userRepository.findTopByUsername(username);
        String encodedPassword = Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString();
        if (user == null || !user.getPassword().equals(encodedPassword)) {
            throw new InvalidSessionException("Invalid username or password");
        }

        Session session = sessionRepository.findFirstByUserUsername(username);
        if (session != null) {
            return session;
        }

        session = new Session();
        session.setUser(user);
        session.setToken(UUID.randomUUID().toString());
        session.setExpireIn(LocalDateTime.now().plus(Duration.of(10, ChronoUnit.MINUTES)));
        return sessionRepository.save(session);
    }

    public void sessionTerminate(String userToken) {
        Session session = sessionRepository.findFirstByToken(userToken);
        if (session != null) {
            sessionRepository.delete(session);
        }
    }

    public User getUserFromToken(String userToken) {
        Session session = sessionRepository.findFirstByToken(userToken);
        if (session == null) {
            throw new InvalidSessionException();
        }
        return session.getUser();
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void cleanExpiredSession() {
        List<Session> expiredSessions = sessionRepository.findExpiredSessions(LocalDateTime.now());
        expiredSessions.stream().forEach(session -> {
            sessionRepository.delete(session);
        });
    }
}
