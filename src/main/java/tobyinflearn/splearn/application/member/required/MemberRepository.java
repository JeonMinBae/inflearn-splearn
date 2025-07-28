package tobyinflearn.splearn.application.member.required;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import tobyinflearn.splearn.domain.member.Member;
import tobyinflearn.splearn.domain.member.Profile;
import tobyinflearn.splearn.domain.shared.Email;

import java.util.Optional;


public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);

    Optional<Member> findByEmail(Email email);

    Optional<Member> findById(Long memberId);

    @Query(" select m from Member m where m.detail.profile = :profile")
    Optional<Object> findByProfile(Profile profile);

}
