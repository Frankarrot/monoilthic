package com.imp.monolithic.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public long signUp(final MemberCreateRequest request) {
        final Member member = request.toEntity();
        final Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }
}
