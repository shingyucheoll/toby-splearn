package tobyspring.splearn.application.required;

import static org.assertj.core.api.Assertions.*;
import static tobyspring.splearn.domain.MemberFixture.*;
import static tobyspring.splearn.domain.MemberFixture.createPasswordEncoder;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import jakarta.persistence.EntityManager;
import tobyspring.splearn.domain.Member;

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
	}
}