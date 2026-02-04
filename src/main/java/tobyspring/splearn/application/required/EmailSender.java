package tobyspring.splearn.application.required;

import org.springframework.stereotype.Component;

import tobyspring.splearn.domain.Email;

/**
 * 이메일을 발송합니다.
 */
@Component
public interface EmailSender {

	void send(Email email, String subject, String body);

}
