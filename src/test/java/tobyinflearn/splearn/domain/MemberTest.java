package tobyinflearn.splearn.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyinflearn.splearn.domain.MemberFixture.memberRegisterRequest;
import static tobyinflearn.splearn.domain.MemberFixture.passwordEncoder;


class MemberTest {

    @Test
    @DisplayName("멤버 생성 시 멤버의 상태는 PENDING 이어야 함")
    void registerMember() {
        var member = member("test@test.test", "테스트");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }


    @Test
    @DisplayName("닉네임이 null일 경우 에러 발생")
    void nicknameNullFail() {
        assertThatThrownBy(() -> member("test@test.test", null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("멤버를 활성화 시 멤버의 상태는 ACTIVE가 되어야 함")
    void activate() {
        var member = member("test@test.test", "테스트");

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("멤버의 상태가 PENDING이 아닐 때 activate를 호출하면 에러 발생")
    void activateFail() {
        var member = member("test@test.test", "테스트");

        member.activate();

        assertThatThrownBy(() -> member.activate()).isInstanceOf(IllegalStateException.class);
    }


    @Test
    @DisplayName("멤버 탈퇴 시 멤버의 상태는 DEACTIVATED가 되어야 함")
    void deactivate() {
        var member = member("test@test.test", "테스트");
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    @DisplayName("멤버의 상태가 ACTIVE가 아닐 때 deactivate를 호출하면 에러 발생")
    void deactivateFail() {
        var member = member("test@test.test", "테스트");

        assertThatThrownBy(() -> member.deactivate())
            .isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();
        assertThatThrownBy(() -> member.deactivate())
            .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("비밀번호 검증 시 올바른 비밀번호는 true, 잘못된 비밀번호는 false를 반환해야 함")
    void verifyPassword() {
        var member = member("test@test.test", "테스트");
        final PasswordEncoder passwordEncoder = passwordEncoder();

        final boolean result = member.verifyPassword("password", passwordEncoder);
        final boolean result2 = member.verifyPassword("notmatch", passwordEncoder);

        assertThat(result).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    @DisplayName("닉네임 변경 시 새로운 닉네임이 적용되어야 함")
    void changeNickname() {
        var member = member("test@test.test", "테스트");

        member.changeNickname("새로운닉네임");

        assertThat(member.getNickname()).isEqualTo("새로운닉네임");
    }

    @Test
    @DisplayName("비밀번호 변경 시 새로운 비밀번호가 적용되어야 함")
    void changePassword() {
        var member = member("test@test.test", "테스트");
        final PasswordEncoder passwordEncoder = passwordEncoder();

        member.changePassword("newPassword", passwordEncoder);

        assertThat(member.verifyPassword("newPassword", passwordEncoder)).isTrue();
    }

    @Test
    @DisplayName("isActive 메서드는 멤버의 상태가 ACTIVE일 때 true를 반환해야 함")
    void isActive() {
        var member = member("test@test.test", "테스트");

        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }
    
    @Test
    @DisplayName("email의 형식이 올바르지 않으면 예외가 발생해야 함")
    void emailFormatFail() {
        assertThatThrownBy(() -> member("invalid-email", "nickname"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private Member member(String email, String nickname) {
        return Member.register(memberRegisterRequest(email, nickname), passwordEncoder());
    }

}