package com.imp.monolithic.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberLoginRequest {

    private String email;
    private String password;

    public MemberLoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
