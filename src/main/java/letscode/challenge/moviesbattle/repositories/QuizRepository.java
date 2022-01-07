package letscode.challenge.moviesbattle.repositories;

import letscode.challenge.moviesbattle.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
