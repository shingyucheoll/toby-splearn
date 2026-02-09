package tobyspring.splearn.adapter.integration;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import tobyspring.splearn.domain.Email;

class DummyEmailSenderTest {

    @Test
    @StdIo
    void dummyEmailSender(StdOut out) {

        DummyEmailSender dummyEmailSender = new DummyEmailSender();

        Email email = new Email("toby@splearn.app");

        dummyEmailSender.send(email, "subject", "body");

        assertThat(out.capturedLines()[0])
            .isEqualTo("DummyEmailSender send email = " + email);

    }

}