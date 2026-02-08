package tobyspring.splearn.application.provided;

import jakarta.validation.Valid;
import tobyspring.splearn.domain.Member;
import tobyspring.splearn.domain.MemberRegisterRequest;

/**
 * 회원의 등록과 관련된 기능을 제공한다.
 */
public interface MemberRegister {
	/*
	Entity를 Return하면 안되는 것 아닌가요?

	Presentation Layer에서 FrontEnd, Client에게 응답할 때,
	필요한 값을 Mapping 하여 전달하는 역할을 갖기 때문에

	Domain Layer에서 Entity를 반환하는것은 크게 문제될 것 없다.
	 */
	Member register(@Valid MemberRegisterRequest registerRequest);
}
