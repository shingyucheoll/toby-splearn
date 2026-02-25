package tobyspring.splearn.application.member.provided;

import tobyspring.splearn.domain.member.Member;

/**
 * 회원을 조회합니다.
 */
public interface MemberFinder {

	Member find(Long memberId);
}
