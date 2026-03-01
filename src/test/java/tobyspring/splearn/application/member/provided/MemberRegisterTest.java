package tobyspring.splearn.application.member.provided;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import tobyspring.splearn.SplearnTestConfiguration;
import tobyspring.splearn.domain.member.DuplicateEmailException;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberFixture;
import tobyspring.splearn.domain.member.MemberInfoUpdateRequest;
import tobyspring.splearn.domain.member.MemberRegisterRequest;
import tobyspring.splearn.domain.member.MemberStatus;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(
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
		Member member = registerMember();

		member = memberRegister.activate(member.getId());

		entitymanager.flush();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
		assertThat(member.getDetail().getActivatedAt()).isNotNull();
	}

	@Test
	void deactivate() {
		Member member = registerMember();

		memberRegister.activate(member.getId());

		entitymanager.flush();
		entitymanager.clear();

		member = memberRegister.deactivate(member.getId());

		assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
		assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
	}

	@Test
	void updateInfo() {
		Member member = registerMember();

		memberRegister.activate(member.getId());

		entitymanager.flush();
		entitymanager.clear();

		member = memberRegister.updateInfo(
			member.getId(),
			new MemberInfoUpdateRequest("ShinGyuCheol", "shin123", "자기소개입니다.")
		);

		assertThat(member.getDetail().getProfile().address()).isEqualTo("shin123");
	}

	private Member registerMember() {
		Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

		entitymanager.flush();
		entitymanager.clear();

		return member;
	}

	@Test
	void memberRegisterRequestFail() {
		checkValidation(new MemberRegisterRequest(
			"toby@splearn.app",
			"Toby",
			"secret"
		));

		checkValidation(new MemberRegisterRequest(
			"toby@splearn.app",
			"Charlie__________________",
			"longsecret"
		));

		checkValidation(new MemberRegisterRequest(
			"toby.splearn.app",
			"Chrrr",
			"longsecret"
		));
	}

	private void checkValidation(MemberRegisterRequest invalid) {
		assertThatThrownBy(() -> memberRegister.register(invalid))
			.isInstanceOf(ConstraintViolationException.class);
	}
}
