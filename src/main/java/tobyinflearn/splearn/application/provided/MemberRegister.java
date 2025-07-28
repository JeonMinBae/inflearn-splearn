package tobyinflearn.splearn.application.provided;

import jakarta.validation.Valid;
import tobyinflearn.splearn.domain.Member;
import tobyinflearn.splearn.domain.MemberRegisterRequest;


public interface MemberRegister {
    Member register(@Valid MemberRegisterRequest registerRequest);

    Member activate(Long memberId);
}
