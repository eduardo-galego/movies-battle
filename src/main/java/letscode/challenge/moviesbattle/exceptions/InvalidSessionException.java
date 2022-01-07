package letscode.challenge.moviesbattle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSessionException extends RuntimeException {

    public InvalidSessionException() {
    }

    public InvalidSessionException(String message) {
        super(message);
    }
}
