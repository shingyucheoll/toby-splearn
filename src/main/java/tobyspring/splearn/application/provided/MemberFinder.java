package tobyspring.splearn.application.provided;

import tobyspring.splearn.domain.Member;

/**
 * 회원을 조회합니다.
 */
public interface MemberFinder {

	Member find(Long memberId);
}
