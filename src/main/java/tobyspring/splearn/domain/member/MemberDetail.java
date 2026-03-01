package tobyspring.splearn.domain.member;

import static org.springframework.util.Assert.*;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tobyspring.splearn.domain.AbstractEntity;

@Entity
@Getter
@ToString
@SuppressWarnings("NullAway.Init")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends AbstractEntity {

    // 자리수, URL에서 사용하기 때문에 영어 소문자 + 숫자로 구성되기 때문에 Value Object로 관리
    private Profile profile;

    private String introduction;

    private LocalDateTime registeredAt;

    private LocalDateTime activatedAt;

    private LocalDateTime deactivatedAt;

    static MemberDetail create() {
        MemberDetail memberDetail = new MemberDetail();
        memberDetail.registeredAt = LocalDateTime.now();
        return memberDetail;
    }

    void activate() {
        isTrue(activatedAt == null, "이미 activatedAt은 설정된 상태입니다.");
        this.activatedAt = LocalDateTime.now();
    }

    void deactivate() {
        isTrue(deactivatedAt == null, "이미 activatedAt은 설정된 상태입니다.");
        this.deactivatedAt = LocalDateTime.now();
    }

    void updateInfo(MemberInfoUpdateRequest updateRequest) {
        this.profile = new Profile(updateRequest.profileAddress());
        this.introduction = Objects.requireNonNull(updateRequest.introduction());
    }
}