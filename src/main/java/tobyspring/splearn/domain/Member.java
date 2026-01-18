package tobyspring.splearn.domain;

import static org.springframework.util.Assert.*;

import java.util.Objects;


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

	private String nickName;

	private String passwordHash;

	private MemberStatus status;

	// Entity의 상태의 변경은 Setter를 사용하지 않고
	// 특정 행위가 발생했을 때 일괄적으로 처리할 수 있도록 메서드 형태로 정의하는게 가장 좋습니다.
	public Member(
		String email,
		String nickName,
		String passwordHash
	) {
		// Not Null 필드에 대해 Objects 에서 지원하는 requireNonNull 사용 - Null일 경우 NPE 발생
		this.email = Objects.requireNonNull(email);
		this.nickName = Objects.requireNonNull(nickName);
		this.passwordHash = Objects.requireNonNull(passwordHash);
		this.status = MemberStatus.PENDING;    // Member 생성 시 상태를 직접 저장합니다.
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
}
