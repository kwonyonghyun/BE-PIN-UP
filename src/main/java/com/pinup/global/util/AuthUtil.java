package com.pinup.global.util;

import com.pinup.entity.Member;
import com.pinup.exception.MemberNotFoundException;
import com.pinup.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    private final MemberRepository memberRepository;

    @Autowired
    public AuthUtil(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getLoginMember() {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        return memberRepository.findByEmail(memberEmail).orElseThrow(MemberNotFoundException::new);
    }
}