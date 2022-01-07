package letscode.challenge.moviesbattle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidAttributeException extends RuntimeException {

    public InvalidAttributeException(String attribute, String value) {
        super("Invalid attribute " + attribute + " value " + value);
    }
}
