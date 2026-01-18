package tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberTest {

	Member member;

	PasswordEncoder passwordEncoder;

	// @BeforeEach: 각 테스트 메서드 실행 전마다 호출
	// @AfterEach: 각 테스트 메서드 실행 후마다 호출
	// @BeforeAll: 클래스의 모든 테스트 실행 전 1회만 호출 (static 메서드)
	// @AfterAll: 클래스의 모든 테스트 실행 후 1회만 호출 (static 메서드)
	@BeforeEach
	void setUp() {
		this.passwordEncoder = new PasswordEncoder() {
			@Override
			public String encode(String password) {
				return password.toUpperCase();
			}

			@Override
			public boolean matches(String password, String passwordHash) {
				return encode(password).equals(passwordHash);
			}
		};

		member = Member.create(
			"toby@splearn.app",
			"Toby",
			"secret",
			passwordEncoder
		);
	}

	@Test
	void createMember() {

		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
	}

	/*
	Live Template - 적극 사용 권장 ( 환경 설정 - 라이브 템플릿 - Java )
	모든 설정에 import 는 모두 추가

	1. @Test 어노테이션 및 method 생성

	단축키: te, 사용 위치: Declaration (선언)
	@org.junit.jupiter.api.Test	 -> 사용하는 어노테이션 및 기능의 라이브러리 모두 명시할 경우 자동으로 import 후 선언됩니다.
	void $TESTNAME$(){			 -> $ $ 형태로 변수를 선언할 경우 cursor의 위치가 자동으로 이동됩니다.
    $END$					     -> 모든 변수를 선언한 이후 마지막 cursor의 위치 명시
	}

	2. assertThat import 및 선언

	단축키: asj , 사용 위치: statement (구문)
	org.assertj.core.api.Assertions.assertThat($ACTUAL$)$END$

	 */
	@Test
	void activate() {
		member.activate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
	}

	@Test
	void activateFail() {
		member.activate();

		assertThatThrownBy(member::activate)
			.isInstanceOf(IllegalStateException.class);
		// IllegalArgumentException -> 파라미터로 전달된 값들이 요구한 값과 다른 경우
		// IllegalStateException -> 호출 자체는 문제 없지만 사용할 수 없는 상태
	}

	@Test
	void deactivate() {
		// given
		member.activate();

		// when
		member.deactivate();

		// then
		assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATED);
	}

	@Test
		// @DisplayName("ACTIVE 상태에서만 DEACTIVATE() 가 가능하다.")
		// 이러한 추가적인 설명은 테스트 코드가 읽히지 않는경우 작성한다. ( 테스트는 최소한의 단위로 빠르게 작성되어야 한다. )
		// given, when, then 과 같은 주석 처리 또한 template 형태로 계속 작성하는게 아닌, 어느정도 TDD 레벨이 올라왔을 땐
		// 개행 정도로만 구분하여 빠르게 테스트를 작성할 수 있도록 합니다.
	void deactivateFail() {
		assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);

		member.activate();
		member.deactivate();

		assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);
	}

	@Test
	void verifyPassword() {
		assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
		assertThat(member.verifyPassword("hello", passwordEncoder)).isFalse();
	}

	@Test
	void changeNickname() {
		assertThat(member.getNickname()).isEqualTo("Toby");
		
		member.changeNickname("Charlie");
		
		assertThat(member.getNickname()).isEqualTo("Charlie");
	}

	@Test
	void changePassword() {
		member.changePassword("verySecret", passwordEncoder);

		assertThat(member.verifyPassword("verySecret", passwordEncoder)).isTrue();

		System.out.println(member.getPasswordHash());

	}
}