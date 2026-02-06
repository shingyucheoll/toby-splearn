package tobyspring.splearn.application.provided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import tobyspring.splearn.application.required.EmailSender;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.MemberStatus;
import tobyspring.splearn.domain.PasswordEncoder;

@SpringBootTest
public class MemberRegisterTest {

    /**
     * 애플리케이션 코드에서는 @Autowired를 사용하여 Bean을 필드로 주입받는 패턴은 지양하는 방법이지만,
     * 테스트 class 의 코드를 다른 코드에서 사용하는 케이스는 없기 때문에 사용해도 괜찮습니다.
     */
    @Autowired
    private MemberRegister memberRegister;

    @Test
    void register() {

        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);

    }

    @TestConfiguration
    static class MemberTestConfiguration {

        @Bean
        public EmailSender emailSender() {
            return (email, _, _) -> System.out.println("Sending email: " + email);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return MemberFixture.createPasswordEncoder();
        }
    }
}
