package tobyinflearn.splearn.application.provided;

import tobyinflearn.splearn.domain.Member;


public interface MemberFinder {
    Member find(Long memberId);
}
