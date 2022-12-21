package com.imp.monolithic.member;

import static com.imp.monolithic.support.MemberFixtures.KUN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.imp.monolithic.support.DatabaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        databaseCleaner.execute();
    }

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

    @DisplayName("회원이 존재하지 않으면 예외가 발생한다.")
    @Test
    void find_notExist_throwsException() {
        // when, then
        assertThatThrownBy(() -> memberService.find(999L)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("회원을 조회한다.")
    @Test
    void find() {
        // given
        final Member member = KUN.create();
        final Member savedMember = memberRepository.save(member);
        final MemberFindResponse expected = MemberFindResponse.from(member);

        // when
        final MemberFindResponse memberFindResponse = memberService.find(savedMember.getId());

        // then
        assertThat(memberFindResponse).usingRecursiveComparison().isEqualTo(expected);
    }
}
