package com.standard.sparta.session;

import lombok.Getter;

@Getter
public class SessionData {

    // 회원 아이디
    private Long memberId;
    // 회원 역할(ADMIN, USER)
    private String memberRole;

    private SessionData(Long memberId, String memberRole) {
        this.memberId = memberId;
        this.memberRole = memberRole;
    }

    /**
     * 세션 데이터 생성
     */
    public static SessionData createNewSessionData(Long memberId, String memberRole) {
        return new SessionData(memberId, memberRole);
    }
}
