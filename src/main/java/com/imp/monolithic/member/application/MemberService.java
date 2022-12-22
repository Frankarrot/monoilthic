package com.imp.monolithic.member.application;

import com.imp.monolithic.member.application.dto.MemberCreateRequest;
import com.imp.monolithic.member.application.dto.MemberFindResponse;
import com.imp.monolithic.member.application.dto.MemberLoginRequest;
import com.imp.monolithic.member.domain.Member;
import com.imp.monolithic.member.domain.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    public MemberService(final MemberRepository memberRepository, final SessionManager sessionManager) {
        this.memberRepository = memberRepository;
        this.sessionManager = sessionManager;
    }

    @Transactional
    public long signUp(final MemberCreateRequest request) {
        final Member member = request.toEntity();
        final Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    public MemberFindResponse find(final Long id) {
        final Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("member not found by id"));

        return MemberFindResponse.from(member);
    }

    public long login(final MemberLoginRequest memberLoginRequest) {
        final Member foundMember = memberRepository.findByEmail(memberLoginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("member not found by email"));
        foundMember.validatePassword(memberLoginRequest.getPassword());
        sessionManager.login(foundMember.getId());
        return foundMember.getId();
    }
}
