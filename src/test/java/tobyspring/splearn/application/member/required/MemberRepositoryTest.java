package tobyspring.splearn.application.member.required;

import static org.assertj.core.api.Assertions.*;
import static tobyspring.splearn.domain.member.MemberFixture.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.persistence.EntityManager;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberStatus;

// 단위 테스트와는 다르게 fake DB(Memory DB)와 JPA동작 방식 검증이 필요하기 때문에 Spring의 도움을 받아 처리합니다.
@DataJpaTest
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	EntityManager entityManager;

	@Test
	void createMember() {
		Member member = Member.register(
			createMemberRegisterRequest(),
			createPasswordEncoder()
		);

		assertThat(member.getId()).isNull();

		memberRepository.save(member);

		assertThat(member.getId()).isNotNull();

		entityManager.flush();
		entityManager.clear();

		var found = memberRepository.findById(member.getId()).orElseThrow();
		assertThat(found.getStatus()).isEqualTo(MemberStatus.PENDING);
		assertThat(found.getDetail().getRegisteredAt()).isNotNull();


	}

	@Test
	void duplicateEmailFail() {
		Member member = Member.register(
			createMemberRegisterRequest(),
			createPasswordEncoder()
		);

		memberRepository.save(member);

		Member member2 = Member.register(
			createMemberRegisterRequest(),
			createPasswordEncoder()
		);

		assertThatThrownBy(() -> memberRepository.save(member2))
			.isInstanceOf(DataIntegrityViolationException.class);

	}
}
