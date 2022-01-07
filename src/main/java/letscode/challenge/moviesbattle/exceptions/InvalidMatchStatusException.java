package letscode.challenge.moviesbattle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMatchStatusException extends RuntimeException {

    public InvalidMatchStatusException(String status, Long matchId) {
        super("Invalid status " + status + " to match " + matchId);
    }
}