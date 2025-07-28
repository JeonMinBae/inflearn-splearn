package tobyinflearn.splearn.application.member.provided;

import jakarta.validation.Valid;
import tobyinflearn.splearn.domain.member.Member;
import tobyinflearn.splearn.domain.member.MemberInfoUpdateRequest;
import tobyinflearn.splearn.domain.member.MemberRegisterRequest;


public interface MemberRegister {
    Member register(@Valid MemberRegisterRequest registerRequest);

    Member activate(Long memberId);

    Member deactivate(Long memberId);

    Member updateInfo(Long memberId, @Valid MemberInfoUpdateRequest memberInfoUpdateRequest);
}
