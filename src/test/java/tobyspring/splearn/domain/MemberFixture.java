package tobyspring.splearn.domain;

// Member & MemberRepository 여러곳에서 사용되는 Member Fixture 생성자를 관리하는 Test Utility Class를 생성합니다.
public class MemberFixture {

	public static MemberRegisterRequest createMemberRegisterRequest(String email) {
		return new MemberRegisterRequest(
			email,
			"Toby",
			"secret"
		);
	}

	public static MemberRegisterRequest createMemberRegisterRequest() {
		return createMemberRegisterRequest("toby@splearn.app");
	}

	public static PasswordEncoder createPasswordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(String password) {
				return password.toUpperCase();
			}

			@Override
			public boolean matches(String password, String passwordHash) {
				return encode(password).equals(passwordHash);
			}
		};
	}

}
