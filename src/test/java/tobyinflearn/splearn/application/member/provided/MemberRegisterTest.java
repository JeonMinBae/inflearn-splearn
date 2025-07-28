package tobyinflearn.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import tobyinflearn.splearn.SplearnTestConfig;
import tobyinflearn.splearn.domain.member.DuplicateEmailException;
import tobyinflearn.splearn.domain.member.DuplicateProfileException;
import tobyinflearn.splearn.domain.member.Member;
import tobyinflearn.splearn.domain.member.MemberInfoUpdateRequest;
import tobyinflearn.splearn.domain.member.MemberRegisterRequest;
import tobyinflearn.splearn.domain.member.MemberStatus;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyinflearn.splearn.domain.memeber.MemberFixture.memberRegisterRequest;


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
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    @DisplayName("멤버 비활성화")
    void deactivate() {
        Member member = registerMember();

        member = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.deactivate(member.getId());

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
    }

    private Member registerMember() {
        Member member = memberRegister.register(memberRegisterRequest("test@test.test", "테스트닉네임"));
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    private Member registerMember(String email) {
        Member member = memberRegister.register(memberRegisterRequest(email, "테스트닉네임"));
        entityManager.flush();
        entityManager.clear();
        return member;
    }

    @Test
    @DisplayName("멤버 업데이트")
    void updateInfo() {
        final MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest("Nickname", "test1234", "자기소개");
        Member member = registerMember();
        member = memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.updateInfo(member.getId(), updateRequest);
        entityManager.flush();
        entityManager.clear();

        assertThat(member.getNickname()).isEqualTo(updateRequest.nickname());
    }


    @Test
    @DisplayName("멤버 업데이트 실패")
    void updateInfoFail() {
        final MemberInfoUpdateRequest updateRequest1 = new MemberInfoUpdateRequest("Nickname", "test1234", "자기소개");
        final MemberInfoUpdateRequest updateRequest2 = new MemberInfoUpdateRequest("Nickname2", "test1234", "자기소개");
        Member member = registerMember();
        member = memberRegister.activate(member.getId());
        memberRegister.updateInfo(member.getId(), updateRequest1);

        Member member2 = registerMember("test2@test.test");
        member2.activate();
        entityManager.flush();
        entityManager.clear();

        assertThatThrownBy(() -> memberRegister.updateInfo(member2.getId(), updateRequest2))
            .isInstanceOf(DuplicateProfileException.class);
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