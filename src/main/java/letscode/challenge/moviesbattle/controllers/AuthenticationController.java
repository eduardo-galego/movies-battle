package letscode.challenge.moviesbattle.controllers;

import letscode.challenge.moviesbattle.data.LoginRequest;
import letscode.challenge.moviesbattle.data.LoginResponse;
import letscode.challenge.moviesbattle.entities.Session;
import letscode.challenge.moviesbattle.services.UserAndSessionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class AuthenticationController {

    private final UserAndSessionService userAndSessionService;

    public AuthenticationController(UserAndSessionService userAndSessionService) {
        this.userAndSessionService = userAndSessionService;
    }

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@ModelAttribute LoginRequest loginRequest) {
        Session session = userAndSessionService.createOrRetrieveSession(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new LoginResponse(session.getToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity login(@RequestHeader("x-usertoken") String userToken) {
        userAndSessionService.sessionTerminate(userToken);
        return ResponseEntity.noContent().build();
    }

}
