package com.standard.sparta.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RequestMapping("/session")
@RestController
@RequiredArgsConstructor
public class SessionController {

    private final SessionRepository sessionRepository;

    @PostMapping
    public ResponseEntity<String> sessionLoginAPI(HttpServletRequest request) {
        log.info("asd");
        // 1. 로그인 로직 처리
        // 아이디, 비밀번호 검증
        // 성공 ...

        HttpSession session = request.getSession();
        Long memberId = 3L;
        String memberRole = "ADMIN";
        session.setAttribute("memberId", memberId);
        session.setAttribute("memberRole", memberRole);

        // 5. 응답 반환
        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<String> sessionRequiredAPI(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }

        Long memberId = (Long) session.getAttribute("memberId");
        String succesStr = "접근 성공 - 회원 아이디: " + memberId;
        return new ResponseEntity<>(succesStr, HttpStatus.OK);
    }

//    /**
//     * 로그인 - 세션 발급
//     */
//    @PostMapping
//    public ResponseEntity<String> sessionLoginAPI() {
//        // 1. 로그인 로직 처리
//        // 아이디, 비밀번호 검증
//        // 성공 ...
//
//        // 2. 생성 - 세션 데이터
//        Long memberId = 1L;
//        String memberRole = "ADMIN";
//        SessionData newSessionData = SessionData.createNewSessionData(memberId, memberRole);
//
//        // 3. 저장 - 세션 데이터 저장
//        String sessionId = sessionRepository.save(newSessionData);
//
//        // 4. 생성 - 헤더 생성
//        String headerValue = "sessionId=" + sessionId;
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Set-Cookie", headerValue);
//
//        // 5. 응답 반환
//        return new ResponseEntity<>("로그인 성공", headers, HttpStatus.OK);
//    }
//
//    /**
//     * 인가가 필요한 API - 세션 조회
//     */
//    @GetMapping
//    public ResponseEntity<String> sessionRequiredAPI(HttpServletRequest request) {
//        // 1. 조회 - 쿠키에서 sessionId 를 조회
//        Cookie foundCookie = findCookie(request)
//                .orElseThrow(() -> new RuntimeException("접근 권한이 없습니다."));
//        String sessionId = foundCookie.getValue();
//        log.info("::: sessionId - {}", sessionId);
//
//        // 2. 조회 - 세션 저장소에서 유저 정보 가져오기
//        SessionData foundSessionData = sessionRepository.getSession(sessionId)
//                .orElseThrow(() -> new RuntimeException("접근 권한이 없습니다."));
//        Long memberId = foundSessionData.getMemberId();
//        String memberRole = foundSessionData.getMemberRole();
//
//        String successStr = "접근 성공 - 회원 아이디: " + memberId;
//
//        // 3. 응답 반환
//        return new ResponseEntity<>(successStr, HttpStatus.OK);
//    }
//
//    /**
//     * 요청 헤더에서 쿠키 찾기
//     */
//    private Optional<Cookie> findCookie(HttpServletRequest request) {
//        if (request.getCookies() == null) {
//            return Optional.empty();
//        }
//        return Arrays.stream(request.getCookies())
//                .filter(cookie -> cookie.getName().equals("sessionId"))
//                .findAny();
//    }
}
