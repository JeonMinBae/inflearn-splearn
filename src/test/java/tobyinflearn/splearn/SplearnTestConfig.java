package tobyinflearn.splearn;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import tobyinflearn.splearn.application.required.EmailSender;
import tobyinflearn.splearn.domain.MemberFixture;
import tobyinflearn.splearn.domain.PasswordEncoder;


@TestConfiguration
public class SplearnTestConfig {

    @Bean
    public EmailSender emailSender() {
        return (email, subject, body) -> {
            System.out.println("이메일 전송: " + email + ", 제목: " + subject + ", 내용: " + body);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return MemberFixture.passwordEncoder();
    }

}
