package tobyinflearn.splearn.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MemberTest {

    @Test
    @DisplayName("멤버 생성 시 멤버의 상태는 PENDING 이어야 함")
    void createMember() {
        var member = new Member("test@test.test", "테스트", "passwordHash");

        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    @DisplayName("닉네임이 null일 경우 에러 발생")
    void nicknameNullFail() {
        assertThatThrownBy(() -> new Member("test@test.test", null, "passwordHash"))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("멤버를 활성화 시 멤버의 상태는 ACTIVE가 되어야 함")
    void activate() {
        var member = new Member("test@test.test", "테스트", "passwordHash");

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("멤버의 상태가 PENDING이 아닐 때 activate를 호출하면 에러 발생")
    void activateFail() {
        var member = new Member("test@test.test", "테스트", "passwordHash");

        member.activate();

        assertThatThrownBy(() -> member.activate()).isInstanceOf(IllegalStateException.class);
    }


    @Test
    @DisplayName("멤버 탈퇴 시 멤버의 상태는 DEACTIVATED가 되어야 함")
    void deactivate() {
        var member = new Member("test@test.test", "테스트", "passwordHash");
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
    }

    @Test
    @DisplayName("멤버의 상태가 ACTIVE가 아닐 때 deactivate를 호출하면 에러 발생")
    void deactivateFail() {
        var member = new Member("test@test.test", "테스트", "passwordHash");

        assertThatThrownBy(() -> member.deactivate())
            .isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();
        assertThatThrownBy(() -> member.deactivate())
            .isInstanceOf(IllegalStateException.class);
    }

}