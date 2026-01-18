package tobyspring.splearn.domain;

// Immutable Object
public record MemberCreateRequest(
	// record component header
	String email,
	String nickname,
	String password
) {
}
