package tobyspring.splearn.domain;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings("NullAway.Init")
@NaturalIdCache
public class Member extends AbstractEntity {

	@NaturalId
	private Email email;

	private String nickname;

	private String passwordHash;

	private MemberStatus status;

	public static Member register(
		MemberRegisterRequest registerRequest,
		PasswordEncoder passwordEncoder
	) {
		Member member = new Member();

		member.email = new Email(registerRequest.email());
		// springframework.util.Assert
		member.nickname = requireNonNull(registerRequest.nickname());
		member.passwordHash = requireNonNull(passwordEncoder.encode(registerRequest.password()));

		member.status = MemberStatus.PENDING;

		return member;
	}

	public void activate() {
		state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

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
