package cart.global.auth;

public class AuthMember {

    private final String email;

    public AuthMember(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
