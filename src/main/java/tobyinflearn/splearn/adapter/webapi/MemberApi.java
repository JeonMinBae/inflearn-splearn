package tobyinflearn.splearn.adapter.webapi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tobyinflearn.splearn.adapter.webapi.dto.MemberRegisterResponse;
import tobyinflearn.splearn.application.member.provided.MemberRegister;
import tobyinflearn.splearn.domain.member.Member;
import tobyinflearn.splearn.domain.member.MemberRegisterRequest;


@RestController
@RequiredArgsConstructor
public class MemberApi {

    private final MemberRegister memberRegister;


    @PostMapping("/api/members")
    public MemberRegisterResponse register(@Valid @RequestBody MemberRegisterRequest request) {
        final Member member = memberRegister.register(request);

        return MemberRegisterResponse.of(member);
    }

}
