package tobyinflearn.splearn.domain;

public class MemberFixture {

    public static MemberRegisterRequest memberRegisterRequest(String email, String nickname) {
        return new MemberRegisterRequest(email, nickname, "password");
    }

    public static PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return "encode" + password;
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }

}
