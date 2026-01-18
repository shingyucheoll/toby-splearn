package tobyspring.splearn.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MemberTest {

	@Test
	void createMember() {
		// Test Code에서는 var 활용하는것을 지향 - 테스트에 조금 더 집중할 수 있도록
		var member = new Member("toby@splearn.app", "Toby", "secret");

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
	void constructorNullCheck() {

		// assertThatThrownBy ( function )  -> 예외가 발생하는지 확인
		// assertThatThrownBy(function).isInstanceOf ( Exception.class ) -> 어떤 타입의 예외가 발생하는지 확인
		assertThatThrownBy(() -> new Member(null, "Toby", "secret"))
			// .isInstanceOf(IOException.class);     -> 다른 타입의 예외 발생 시 테스트 실패
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void activate() {
		var member = new Member("toby", "Toby", "secret");

		member.activate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
	}

	@Test
	void activateFail() {
		var member = new Member("toby", "Toby", "secret");

		member.activate();

		assertThatThrownBy(member::activate)
			.isInstanceOf(IllegalStateException.class);
		// IllegalArgumentException -> 파라미터로 전달된 값들이 요구한 값과 다른 경우
		// IllegalStateException -> 호출 자체는 문제 없지만 사용할 수 없는 상태
	}

	@Test
	void deactivate() {
		// given
		var member = new Member("toby", "Toby", "secret");
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
		var member = new Member("toby", "Toby", "secret");

		assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);

		member.activate();
		member.deactivate();

		assertThatThrownBy(member::deactivate).isInstanceOf(IllegalStateException.class);
	}
}