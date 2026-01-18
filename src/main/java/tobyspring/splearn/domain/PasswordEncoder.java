package tobyspring.splearn.domain;

public interface PasswordEncoder {
	String encode(String password);
	// 입력받은 비밀번호와 Hash로 저장된 비밀번호가 일치하는지 확인
	boolean matches(String password, String passwordHash);
}
