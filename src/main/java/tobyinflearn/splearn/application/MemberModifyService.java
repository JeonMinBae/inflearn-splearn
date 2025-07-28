package tobyinflearn.splearn.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyinflearn.splearn.application.provided.MemberFinder;
import tobyinflearn.splearn.application.provided.MemberRegister;
import tobyinflearn.splearn.application.required.EmailSender;
import tobyinflearn.splearn.application.required.MemberRepository;
import tobyinflearn.splearn.domain.DuplicateEmailException;
import tobyinflearn.splearn.domain.Email;
import tobyinflearn.splearn.domain.Member;
import tobyinflearn.splearn.domain.MemberRegisterRequest;
import tobyinflearn.splearn.domain.PasswordEncoder;


@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {

    private final MemberFinder memberFinder;
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        checkDuplicateEmail(registerRequest);

        final Member member = Member.register(registerRequest, passwordEncoder);

        memberRepository.save(member);

        sendWelcomeEmail(member);

        return member;
    }

    @Override
    public Member activate(Long memberId) {
        final Member member = memberFinder.find(memberId);

        member.activate();

        return memberRepository.save(member);
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요", "아래 링크를 통해 등록을 완료해주세요");
    }

    private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
        if (memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()){
            throw new DuplicateEmailException("이미 등록된 이메일입니다: " + registerRequest.email());
        }
    }

}
