package tobyinflearn.splearn.domain.member;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;
import tobyinflearn.splearn.domain.AbstractEntity;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;


@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends AbstractEntity {

    private Profile profile;
    private String introduction;
    private LocalDateTime registeredAt;
    private LocalDateTime activatedAt;
    private LocalDateTime deactivatedAt;

    static MemberDetail create() {
        MemberDetail detail = new MemberDetail();
        detail.registeredAt = LocalDateTime.now();
        return detail;
    }

    void activate() {
        Assert.isTrue(activatedAt == null, "이미 활성화된 회원입니다.");

        activatedAt = LocalDateTime.now();
    }

    void deactivate() {
        Assert.isTrue(deactivatedAt == null, "이미 비활성화된 회원입니다.");

         deactivatedAt = LocalDateTime.now();
    }

    void updateInfo(MemberInfoUpdateRequest updateRequest) {
        profile = new Profile(updateRequest.profileAddress());
        introduction = requireNonNull(updateRequest.introduction());
    }

}
