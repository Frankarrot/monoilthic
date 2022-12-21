package com.imp.monolithic.member.application.dto;

import com.imp.monolithic.member.domain.Member;
import com.imp.monolithic.member.domain.Role;

public class MemberFindResponse {

    private final long id;

    private final String email;

    private final Role role;

    private final String nickname;

    private final long point;

    public MemberFindResponse(final long id, final String email, final Role role, final String nickname,
                              final long point) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.nickname = nickname;
        this.point = point;
    }

    public static MemberFindResponse from(final Member member) {
        return new MemberFindResponse(member.getId(), member.getEmail(), member.getRole(), member.getNickname(),
                member.getPoint());
    }
}
