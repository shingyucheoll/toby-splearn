package tobyspring.splearn.domain;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Getter, ToString 과 같은 어노테이션은 적극 활용하여 도메인이 갖는 속성 및 상태 전이 로직 가독성에 영향을 끼치지 않도록 합니다.
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SuppressWarnings("NullAway.Init")  // JPA requires no-arg constructor for lazy initialization
public class Member {

	@Id
	private String email;

	private String nickname;

	private String passwordHash;

	private MemberStatus status;

	public static Member create(
		MemberCreateRequest createRequest,
		PasswordEncoder passwordEncoder
	) {
		Member member = new Member();
		member.email = requireNonNull(createRequest.email());
		member.nickname = requireNonNull(createRequest.nickname());
		member.passwordHash = requireNonNull(passwordEncoder.encode(createRequest.password()));

		member.status = MemberStatus.PENDING;

		return member;
	}

	public void activate() {
		// org.springframework.util.Assert; 라이브러리는
		// Apache Commons, Google Guava 와 다를 것 없는 유틸리티 클래스로 사용해도 좋습니다.
		// 상태가 pending이 아닌 경우 - state(!expression, "error Msg") 형태
		state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

		// 제거할 수 있는 코드
		// if (status != MemberStatus.PENDING) {
		// 	throw new IllegalStateException("PENDING 상태가 아닙니다.");
		// }
		this.status = MemberStatus.ACTIVATE;
	}

	public void deactivate() {
		state(status == MemberStatus.ACTIVATE, "ACTIVE 상태가 아닙니다.");

		this.status = MemberStatus.DEACTIVATED;
	}

	public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(password, passwordHash);
	}

	public void changeNickname(String nickname) {
		this.nickname = requireNonNull(nickname);
	}

	public void changePassword(String password, PasswordEncoder passwordEncoder) {
		this.passwordHash = passwordEncoder.encode(requireNonNull(password));
	}

	public boolean isActive() {
		return status == MemberStatus.ACTIVATE;
	}
}
