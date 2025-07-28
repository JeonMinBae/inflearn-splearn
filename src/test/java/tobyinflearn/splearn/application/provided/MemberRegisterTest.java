package tobyinflearn.splearn.application.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import tobyinflearn.splearn.SplearnTestConfig;
import tobyinflearn.splearn.domain.DuplicateEmailException;
import tobyinflearn.splearn.domain.Member;
import tobyinflearn.splearn.domain.MemberRegisterRequest;
import tobyinflearn.splearn.domain.MemberStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyinflearn.splearn.domain.MemberFixture.memberRegisterRequest;


@Import(SplearnTestConfig.class)
@Transactional
@SpringBootTest
record MemberRegisterTest(MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    @DisplayName("멤버 등록 테스트")
    void register() {
        final Member member = memberRegister.register(memberRegisterRequest("test@test.test", "테스트닉네임"));

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    @DisplayName("멤버 이메일 중복 에러")
    void registerFail() {
        memberRegister.register(memberRegisterRequest("test@test.test", "테스트닉네임"));

        assertThatThrownBy(() -> memberRegister.register(memberRegisterRequest("test@test.test", "테스트닉네임")))
            .isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    @DisplayName("멤버 활성화")
    void activate() {
        Member member = memberRegister.register(memberRegisterRequest("test@test.test", "테스트닉네임"));
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.activate(member.getId());
        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);

    }

    @Test
    @DisplayName("멤버 등록 시 파라메터 형식 실패")
    void registerFailParameter() {
        final MemberRegisterRequest registerRequest = new MemberRegisterRequest("test@test.test", "테스트", "password");
        assertThatThrownBy(() -> memberRegister.register(registerRequest)).isInstanceOf(ConstraintViolationException.class);

        final MemberRegisterRequest registerRequest2 = new MemberRegisterRequest("test@test.test", "테스트닉네임", "passwd");
        assertThatThrownBy(() -> memberRegister.register(registerRequest2)).isInstanceOf(ConstraintViolationException.class);

        final MemberRegisterRequest registerRequest3 = new MemberRegisterRequest("testtest.test", "테스트", "password");
        assertThatThrownBy(() -> memberRegister.register(registerRequest3)).isInstanceOf(ConstraintViolationException.class);
    }

}