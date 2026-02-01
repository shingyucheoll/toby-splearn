package tobyspring.splearn.domain;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// Getter, ToString 과 같은 어노테이션은 적극 활용하여
// 도메인이 갖는 속성 및 상태 전이 로직의 가독성에 영향을 끼치지 않도록 합니다.
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings("NullAway.Init")  // JPA requires no-arg constructor for lazy initialization
@NaturalIdCache
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// JPA Entity에 JPA에서 허용한 기본 타입 (String, Integer, Long ...)이 아닌
	// Custom Type을 선언할 경우 Embedded Object로 만들어야 합니다.
	// JPA Embedded: 해당 필드가 다른곳에서 정의된 Embeddable 타입을 내장하고 있음을 나타내며
	// 해당 필드의 속성들이 엔티티 테이블의 컬럼으로 포함됩니다.
	@Embedded
	@NaturalId
    private Email email;

    private String nickname;

    private String passwordHash;

	// Entity 필드값을 Enum으로 사용하고 있을 경우 @Enumerated 를 붙여서 관리하는게 좋습니다.
	// DB및 Enum의 옵션에 따라 내부 기능을 확장 가능합니다.
	@Enumerated(EnumType.STRING)
    private MemberStatus status;

    public static Member register(
        MemberRegisterRequest registerRequest,
        PasswordEncoder passwordEncoder
    ) {
        Member member = new Member();

        member.email = new Email(registerRequest.email());
        member.nickname = requireNonNull(registerRequest.nickname());
        member.passwordHash = requireNonNull(passwordEncoder.encode(registerRequest.password()));

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
