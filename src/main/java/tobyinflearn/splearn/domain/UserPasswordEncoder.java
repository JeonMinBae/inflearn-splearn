package tobyinflearn.splearn.domain;

public class UserPasswordEncoder implements PasswordEncoder{

    @Override
    public String encode(String password) {
        return "";
    }

    @Override
    public boolean matches(String password, String passwordHash) {
        return false;
    }

}
