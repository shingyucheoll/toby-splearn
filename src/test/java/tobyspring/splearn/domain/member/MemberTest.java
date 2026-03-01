package tobyspring.splearn.domain.member;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static tobyspring.splearn.domain.member.MemberFixture.*;

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
		this.passwordEncoder = createPasswordEncoder();

		MemberRegisterRequest memberRegisterRequest = createMemberRegisterRequest();

		member = Member.register(memberRegisterRequest, passwordEncoder);
	}

	@Test
	void registerMember() {

		assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
		// Clock을 사용하여 메서드가 실행되기 전 시간을 제어하여 동일한 시간 정보를 통해 테스트를 할 수 있지만
		// 간단하게 값이 설정되었는지만 체크하여 검증합니다.
		assertThat(member.getDetail().getRegisteredAt()).isNotNull();
	}

	@Test
	void activate() {
		member.activate();

		assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVATE);
		assertThat(member.getDetail().getActivatedAt()).isNotNull();
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
		assertThat(member.getDetail().getDeactivatedAt()).isNotNull();
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
		assertThat(member.verifyPassword("verySecret", passwordEncoder)).isTrue();
		assertThat(member.verifyPassword("verySecret2", passwordEncoder)).isFalse();
	}

	@Test
	void changePassword() {
		member.changePassword("verySecret2", passwordEncoder);

		assertThat(member.verifyPassword("verySecret2", passwordEncoder)).isTrue();

		System.out.println(member.getPasswordHash());

	}

	@Test
	void isActive() {
		assertThat(member.isActive()).isFalse();

		member.activate();

		assertThat(member.isActive()).isTrue();

		member.deactivate();

		assertThat(member.isActive()).isFalse();
	}

	@Test
	void invalidEmail() {
		assertThatThrownBy(() ->
			Member.register(createMemberRegisterRequest("invalid email"), passwordEncoder)
		).isInstanceOf(IllegalArgumentException.class);

		Member.register(createMemberRegisterRequest(), passwordEncoder);
	}

	@Test
	void updateInfo() {
		member.activate();

		var request = new MemberInfoUpdateRequest("Shin", "shin123", "자기소개입니다.");

		member.updateInfo(new MemberInfoUpdateRequest("Shin", "shin123", "자기소개입니다."));

		assertThat(member.getNickname()).isEqualTo(request.nickname());
		assertThat(member.getDetail().getProfile().address()).isEqualTo(request.profileAddress());
		assertThat(member.getDetail().getIntroduction()).isEqualTo(request.introduction());
	}

	@Test
	void updateInfoFail() {
		assertThatThrownBy(() -> {
			var request = new MemberInfoUpdateRequest("Shin", "shin123", "자기소개");
			member.updateInfo(request);
		})
		    .isInstanceOf(IllegalStateException.class);

	}
}