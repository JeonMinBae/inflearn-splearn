package tobyinflearn.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import tobyinflearn.splearn.SplearnTestConfig;
import tobyinflearn.splearn.domain.member.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyinflearn.splearn.domain.memeber.MemberFixture.memberRegisterRequest;


@Import(SplearnTestConfig.class)
@Transactional
@SpringBootTest
record MemberFinderTest(MemberFinder memberFinder, MemberRegister memberRegister, EntityManager entityManager) {

    @Test
    @DisplayName("멤버 조회 테스트")
    void finder() {
        final Member member = memberRegister.register(memberRegisterRequest("test@test.test", "테스트닉네임"));
        entityManager.flush();
        entityManager.clear();

        final Member foundMember = memberFinder.find(member.getId());

        assertThat(foundMember.getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("멤버 조회 실패")
    void finderFail() {
        assertThatThrownBy(() -> memberFinder.find(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }



}