package tobyinflearn.splearn.application.member.required;

import tobyinflearn.splearn.domain.shared.Email;


public interface EmailSender {
    void send(Email email, String subject, String body);
}
