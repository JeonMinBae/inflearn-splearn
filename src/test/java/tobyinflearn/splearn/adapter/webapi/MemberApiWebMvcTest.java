package tobyinflearn.splearn.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tobyinflearn.splearn.application.member.provided.MemberRegister;
import tobyinflearn.splearn.domain.memeber.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tobyinflearn.splearn.domain.memeber.MemberFixture.memberRegisterRequest;


@WebMvcTest(MemberApi.class)
@RequiredArgsConstructor
class MemberApiWebMvcTest {
    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;

    @MockitoBean
    MemberRegister memberRegister;


    @Test
    void apiTest() throws JsonProcessingException {
        final String content = objectMapper.writeValueAsString(memberRegisterRequest());

        when(memberRegister.register(any())).thenReturn(MemberFixture.member((1L)));

        assertThat(mvcTester.post().uri("/api/members").contentType("application/json").content(content))
            .hasStatusOk()
            .bodyJson()
            .extractingPath("$.memberId").asNumber().isEqualTo(1);
        verify(memberRegister).register(any());
    }

    @Test
    void apiFailTest() throws JsonProcessingException {
        final String content = objectMapper.writeValueAsString(memberRegisterRequest("invalid-email", "테스트닉네임"));

        assertThat(mvcTester.post().uri("/api/members").contentType("application/json").content(content))
            .hasStatus(HttpStatus.BAD_REQUEST);
    }

}