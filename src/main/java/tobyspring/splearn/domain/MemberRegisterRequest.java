package tobyspring.splearn.domain;

// Immutable Object
public record MemberRegisterRequest(
	// record component header
	String email,
	String nickname,
	String password
) {
}
