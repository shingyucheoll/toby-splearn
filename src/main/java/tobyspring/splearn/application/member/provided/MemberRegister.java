package tobyspring.splearn.application.member.provided;

import jakarta.validation.Valid;
import tobyspring.splearn.domain.member.Member;
import tobyspring.splearn.domain.member.MemberInfoUpdateRequest;
import tobyspring.splearn.domain.member.MemberRegisterRequest;

/**
 * 회원의 등록과 관련된 기능을 제공한다.
 * Application Layer의 Port에서 Entity를 반환하게 될 경우
 * Presentation Layer에서 필요한 값만 Mapping하여 전달할 수 있도록 로직을 구성해야 합니다.
 */
public interface MemberRegister {

	Member register(@Valid MemberRegisterRequest registerRequest);

	// Client측에서 알고있는 식별자 ID를 사용하여 처리합니다.
	Member activate(Long memberId);

	Member deactivate(Long memberId);

	Member updateInfo(Long memberId, @Valid MemberInfoUpdateRequest memberInfoUpdateRequest);
}
