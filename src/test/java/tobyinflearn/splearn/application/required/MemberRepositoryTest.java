package tobyinflearn.splearn.application.required;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import tobyinflearn.splearn.domain.Member;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyinflearn.splearn.domain.MemberFixture.memberRegisterRequest;
import static tobyinflearn.splearn.domain.MemberFixture.passwordEncoder;


@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("멤버 생성")
    void createMember() {
        final Member member =
            Member.register(memberRegisterRequest("test@test.test", "testNickname"), passwordEncoder());

        assertThat(member.getId()).isNull();

        final Member save = memberRepository.save(member);

        assertThat(save.getId()).isEqualTo(1L);
        em.flush();

    }

    @Test
    @DisplayName("이메일은 중복되어 저장될 수 없음")
    void emailDuplicateFail() {
        final Member member =
            Member.register(memberRegisterRequest("test@test.test", "testNickname"), passwordEncoder());
            memberRepository.save(member);

        final Member member2 =
            Member.register(memberRegisterRequest("test@test.test", "testNickname"), passwordEncoder());
        assertThatThrownBy(() -> {
            memberRepository.save(member2);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

}