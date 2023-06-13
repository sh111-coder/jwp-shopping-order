package cart.member.domain;

import javax.persistence.*;

@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String password;

    protected Member() {
    }

    private Member(final Long id, final String email,
                   final String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static class Builder {

        private Long id;
        private String email;
        private String password;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Builder password(final String password) {
            this.password = password;
            return this;
        }

        public Member build() {
            return new Member(id, email, password);
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
