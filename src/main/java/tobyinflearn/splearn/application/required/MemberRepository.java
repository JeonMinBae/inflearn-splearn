package tobyinflearn.splearn.application.required;

import org.springframework.data.repository.Repository;
import tobyinflearn.splearn.domain.Email;
import tobyinflearn.splearn.domain.Member;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);

    Optional<Member> findByEmail(Email email);

    Optional<Member> findById(Long memberId);

}
