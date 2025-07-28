package tobyinflearn.splearn.application.member.provided;

import tobyinflearn.splearn.domain.member.Member;


public interface MemberFinder {
    Member find(Long memberId);
}
