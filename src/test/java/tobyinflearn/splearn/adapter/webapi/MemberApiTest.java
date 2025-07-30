package tobyinflearn.splearn.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import tobyinflearn.splearn.AssertThatUtils;
import tobyinflearn.splearn.adapter.webapi.dto.MemberRegisterResponse;
import tobyinflearn.splearn.application.member.provided.MemberRegister;
import tobyinflearn.splearn.application.member.required.MemberRepository;
import tobyinflearn.splearn.domain.member.Member;
import tobyinflearn.splearn.domain.member.MemberRegisterRequest;
import tobyinflearn.splearn.domain.member.MemberStatus;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static tobyinflearn.splearn.domain.memeber.MemberFixture.memberRegisterRequest;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class MemberApiTest {

    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;
    final MemberRepository memberRepository;
    final MemberRegister memberRegister;

    @Test
    void register() throws JsonProcessingException, UnsupportedEncodingException {
        final MemberRegisterRequest request = memberRegisterRequest();
        final String content = objectMapper.writeValueAsString(request);

        final MvcTestResult result =
            mvcTester.post().uri("/api/members").contentType("application/json").content(content).exchange();
        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPathSatisfying("$.memberId", AssertThatUtils.notNull())
            .hasPathSatisfying("$.email", AssertThatUtils.eqTo(request.email()));

        final var response =
            objectMapper.readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);

        final Member member = memberRepository.findById(response.memberId()).orElseThrow();
        assertThat(member.getEmail().address()).isEqualTo(request.email());
        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmail() throws JsonProcessingException {
        memberRegister.register(memberRegisterRequest());

        final MemberRegisterRequest request = memberRegisterRequest();
        final String content = objectMapper.writeValueAsString(request);

        mvcTester.post().uri("/api/members").contentType("application/json").content(content).exchange();

        final MvcTestResult result =
            mvcTester.post().uri("/api/members").contentType("application/json").content(content).exchange();

        assertThat(result)
            .apply(MockMvcResultHandlers.print())
            .hasStatus(HttpStatus.CONFLICT);
    }

}