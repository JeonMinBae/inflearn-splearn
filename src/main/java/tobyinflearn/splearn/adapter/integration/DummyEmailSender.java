package tobyinflearn.splearn.adapter.integration;

import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;
import tobyinflearn.splearn.application.member.required.EmailSender;
import tobyinflearn.splearn.domain.shared.Email;


@Fallback
@Component
public class DummyEmailSender implements EmailSender {

    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("DummyEmailSender send email: " + email);
    }

}
