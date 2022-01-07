package letscode.challenge.moviesbattle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMatchActionException extends RuntimeException {

    public InvalidMatchActionException(String message) {
        super(message);
    }
}
