package letscode.challenge.moviesbattle.repositories;


import letscode.challenge.moviesbattle.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findTopByUsername(String username);

}