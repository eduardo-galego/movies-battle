package letscode.challenge.moviesbattle.data;

public class LoginResponse {

    private String usertoken;

    public String getUsertoken() {
        return usertoken;
    }

    public void setUsertoken(String usertoken) {
        this.usertoken = usertoken;
    }

    public LoginResponse(String usertoken) {
        this.usertoken = usertoken;
    }
}
