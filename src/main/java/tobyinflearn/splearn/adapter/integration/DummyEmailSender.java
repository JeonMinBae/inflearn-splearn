package tobyinflearn.splearn.adapter.integration;

import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Component;
import tobyinflearn.splearn.application.required.EmailSender;
import tobyinflearn.splearn.domain.Email;


@Fallback
@Component
public class DummyEmailSender implements EmailSender {

    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("DummyEmailSender send email: " + email);
    }

}
