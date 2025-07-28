package tobyinflearn.splearn.domain;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import org.springframework.util.Assert;

import static java.util.Objects.requireNonNull;

@Getter
@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity{

    @NaturalId //
    private Email email;
    private String nickname;
    private String passwordHash;
    private MemberStatus status;


    public static Member register(MemberRegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
        final Member member = new Member();
        member.email = new Email(registerRequest.email());
        member.nickname = requireNonNull(registerRequest.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(registerRequest.password()));
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
