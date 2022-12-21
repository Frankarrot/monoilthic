package com.imp.monolithic.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberCreateRequest {

    private String email;
    private String password;
    private String nickname;
    private Role role;

    public MemberCreateRequest(final String email, final String password, final String nickname, final Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public Member toEntity() {
        return new Member(role, email, nickname, password);
    }
}
