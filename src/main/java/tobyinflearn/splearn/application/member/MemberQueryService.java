package tobyinflearn.splearn.application.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyinflearn.splearn.application.member.provided.MemberFinder;
import tobyinflearn.splearn.application.member.required.EmailSender;
import tobyinflearn.splearn.application.member.required.MemberRepository;
import tobyinflearn.splearn.domain.member.Member;
import tobyinflearn.splearn.domain.member.PasswordEncoder;


@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class MemberQueryService implements MemberFinder {

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Member find(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다: " + memberId));
    }

}
