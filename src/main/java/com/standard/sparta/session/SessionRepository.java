package com.standard.sparta.session;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionRepository {

    // 세션 저장소(메모리 데이터베이스: redis)
    // sessionKey : 세션데이터
    private Map<String, SessionData> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 저장
     */
    public String save(SessionData sessionData) {
        // 1. 생성: 세션 아이디
        String sessionId = UUID.randomUUID().toString();

        // 2. 저장: 세션 저장
        sessionStore.put(sessionId, sessionData);

        // 3. 반환: 세션 아이디
        return sessionId;
    }

    /**
     * 세션 조회
     */
    public Optional<SessionData> getSession(String sessionId) {
        SessionData sessionData = sessionStore.get(sessionId);
        return Optional.ofNullable(sessionData);
    }
}
