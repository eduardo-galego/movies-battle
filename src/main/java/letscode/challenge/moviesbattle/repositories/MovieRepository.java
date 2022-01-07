package letscode.challenge.moviesbattle.repositories;

import letscode.challenge.moviesbattle.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Integer findTopByOrderByIdDesc();
}