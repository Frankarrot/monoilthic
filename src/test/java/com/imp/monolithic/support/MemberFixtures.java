package com.imp.monolithic.support;

import com.imp.monolithic.member.domain.Member;
import com.imp.monolithic.member.domain.Role;

public enum MemberFixtures {

    KUN("ghd700@email.com", Role.ADMIN, "password", "kun"),
    ;

    private final String email;
    private final Role role;

    private final String password;

    private final String nickname;

    MemberFixtures(final String email, final Role role, final String password, final String nickname) {
        this.email = email;
        this.role = role;
        this.password = password;
        this.nickname = nickname;
    }

    public Member create() {
        return new Member(role, email, nickname, password);
    }
}
