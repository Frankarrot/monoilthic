package com.imp.monolithic.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원가입을 완료한다.")
    void signUp() {
        // given
        String email = "email";
        String password = "password";
        String nickname = "nickname";
        Role role = Role.ADMIN;
        MemberCreateRequest request = new MemberCreateRequest(email, password, nickname, role);

        // when
        long id = memberService.signUp(request);

        // then
        assertThat(id).isNotZero();
    }
}
