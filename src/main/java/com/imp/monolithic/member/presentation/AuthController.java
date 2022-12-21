package com.imp.monolithic.member.presentation;

import com.imp.monolithic.member.application.MemberService;
import com.imp.monolithic.member.application.dto.MemberCreateRequest;
import com.imp.monolithic.member.application.dto.MemberFindResponse;
import com.imp.monolithic.member.application.dto.MemberLoginRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/members")
public class AuthController {

    private final MemberService memberService;

    public AuthController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody final MemberCreateRequest memberCreateRequest) {
        final long id = memberService.signUp(memberCreateRequest);
        return ResponseEntity.created(URI.create("/members" + id)).build();
    }

    @GetMapping("/me")
    public ResponseEntity<MemberFindResponse> find(@SessionAttribute("id") final Long id) {
        final MemberFindResponse memberFindResponse = memberService.find(id);
        return ResponseEntity.ok(memberFindResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody final MemberLoginRequest memberLoginRequest) {
        final long id = memberService.login(memberLoginRequest);
        return ResponseEntity.ok(id);
    }
}
