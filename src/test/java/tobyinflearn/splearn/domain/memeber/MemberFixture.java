package tobyinflearn.splearn.domain.memeber;

import org.springframework.test.util.ReflectionTestUtils;
import tobyinflearn.splearn.domain.member.Member;
import tobyinflearn.splearn.domain.member.MemberRegisterRequest;
import tobyinflearn.splearn.domain.member.PasswordEncoder;


public class MemberFixture {

    public static MemberRegisterRequest memberRegisterRequest() {
        return new MemberRegisterRequest("test@test.test", "nickname", "password");
    }

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

    public static Member member(Long id) {
        final Member member = Member.register(memberRegisterRequest(), passwordEncoder());
        ReflectionTestUtils.setField(member, "id", id);

        return member;
    }

}
