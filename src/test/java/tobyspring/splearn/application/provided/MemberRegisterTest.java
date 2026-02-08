package tobyspring.splearn.application.provided;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.DuplicateEmailException;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberFixture;
import tobyspring.splearn.domain.MemberRegisterRequest;
import tobyspring.splearn.domain.MemberStatus;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
public record MemberRegisterTest(
	MemberRegister memberRegister,
	EntityManager entitymanager
) {

	@Test
	void register() {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

		assertThat(member.getId()).isNotNull();
		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	@Test
	void duplicateEmailFail() {
		memberRegister.register(MemberFixture.createMemberRegisterRequest());

		assertThatThrownBy(() -> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
			.isInstanceOf(DuplicateEmailException.class);
	}

	@Test
	void activate() {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
		
		entitymanager.flush();
		entitymanager.clear();

		member = memberRegister.activate(member.getId());

		entitymanager.flush();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
	}

	@Test
	void memberRegisterRequestFail() {
		invalidRequest(new MemberRegisterRequest(
			"toby@splearn.app",
			"Toby",
			"secret"
		));

		invalidRequest(new MemberRegisterRequest(
			"toby@splearn.app",
			"Charlie__________________",
			"longsecret"
		));

		invalidRequest(new MemberRegisterRequest(
			"toby.splearn.app",
			"Chrrr",
			"longsecret"
		));
	}

	private void invalidRequest(MemberRegisterRequest invalid) {
		assertThatThrownBy(() -> memberRegister.register(invalid))
		    .isInstanceOf(ConstraintViolationException.class);
	}
}
