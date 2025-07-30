package tobyinflearn.splearn.application.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tobyinflearn.splearn.adapter.security.SecurePasswordEncoder;
import tobyinflearn.splearn.application.member.provided.MemberFinder;
import tobyinflearn.splearn.application.member.provided.MemberRegister;
import tobyinflearn.splearn.application.member.required.EmailSender;
import tobyinflearn.splearn.application.member.required.MemberRepository;
import tobyinflearn.splearn.domain.member.DuplicateEmailException;
import tobyinflearn.splearn.domain.member.DuplicateProfileException;
import tobyinflearn.splearn.domain.member.Member;
import tobyinflearn.splearn.domain.member.MemberInfoUpdateRequest;
import tobyinflearn.splearn.domain.member.MemberRegisterRequest;
import tobyinflearn.splearn.domain.member.PasswordEncoder;
import tobyinflearn.splearn.domain.member.Profile;
import tobyinflearn.splearn.domain.shared.Email;

import java.util.Objects;


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

    @Override
    public Member deactivate(Long memberId) {
        final Member member = memberFinder.find(memberId);

        member.deactivate();

        return memberRepository.save(member);
    }

    @Override
    public Member updateInfo(Long memberId, MemberInfoUpdateRequest memberInfoUpdateRequest) {
        final Member member = memberFinder.find(memberId);

        checkDuplicateProfile(member, memberInfoUpdateRequest.profileAddress());

        member.updateInfo(memberInfoUpdateRequest);

        return memberRepository.save(member);
    }

    private void checkDuplicateProfile(Member member, String profileAddress) {
        if (profileAddress.isEmpty()) { return; }

        final Profile currentProfile = member.getDetail().getProfile();
        if (Objects.nonNull(currentProfile) && Objects.equals(currentProfile.address(), profileAddress)) { return; }

        if (memberRepository.findByProfile(new Profile(profileAddress)).isPresent()) {
            throw new DuplicateProfileException("이미 등록된 프로필 주소입니다: " + profileAddress);
        }
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
