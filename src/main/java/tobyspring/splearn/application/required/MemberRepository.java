package tobyspring.splearn.application.required;

import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import tobyspring.splearn.domain.Member;

/**
 * 회원 정보를 저장하거나 조회합니다.
 */
@Component
public interface MemberRepository extends Repository<Member, Long> {

	Member save(Member member);
}
