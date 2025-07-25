package tobyinflearn.splearn.domain;

import lombok.Getter;
import org.springframework.util.Assert;

import static java.util.Objects.requireNonNull;


@Getter
public class Member {

    private Email email;
    private String nickname;
    private String passwordHash;
    private MemberStatus status;


    private Member() {
    }

    public static Member create(MemberCreateRequest createRequest, PasswordEncoder passwordEncoder) {
        final Member member = new Member();
        member.email = new Email(createRequest.email());
        member.nickname = requireNonNull(createRequest.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(createRequest.password()));
        member.status = MemberStatus.PENDING;

        return member;
    }


    public void activate() {
        Assert.state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

        status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        Assert.state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

        status = MemberStatus.DEACTIVATED;
    }

    public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, passwordHash);
    }

    public void changeNickname(String newNickname) {
        nickname = requireNonNull(newNickname);
    }

    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.passwordHash = passwordEncoder.encode(requireNonNull(newPassword));
    }

    public boolean isActive() {
        return status == MemberStatus.ACTIVE;
    }

}
