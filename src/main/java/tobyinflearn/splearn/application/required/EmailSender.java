package tobyinflearn.splearn.application.required;

import tobyinflearn.splearn.domain.Email;


public interface EmailSender {
    void send(Email email, String subject, String body);
}
