package cart.global.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cart.global.auth.exception.AuthenticationException;
import cart.global.exception.ErrorCode;
import cart.member.application.MemberService;
import cart.member.domain.Member;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String BASIC_AUTH_PREFIX = "basic";
    private final MemberService memberService;

    public AuthInterceptor(final MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        checkAuthHeader(authorization);

        String[] authHeader = authorization.split(" ");
        checkAuthPrefix(authHeader);

        byte[] decodedBytes = Base64.decodeBase64(authHeader[1]);
        String decodedString = new String(decodedBytes);
        String[] credentials = decodedString.split(":");
        String email = credentials[0];
        String password = credentials[1];

        // 본인 여부 확인
        checkMemberAuthentication(email, password);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private void checkAuthHeader(String authorization) {
        if (authorization == null) {
            throw new AuthenticationException(ErrorCode.NOT_EXISTS_AUTHORIZATION_HEADER);
        }
    }

    private void checkAuthPrefix(String[] authHeader) {
        if (!authHeader[0].equalsIgnoreCase(BASIC_AUTH_PREFIX)) {
            throw new AuthenticationException(ErrorCode.NOT_EXISTS_AUTHORIZATION_PREFIX);
        }
    }

    private void checkMemberAuthentication(String email, String password) {
        Member member = memberService.findByMemberAuthInfo(email)
                .orElseThrow(() -> new AuthenticationException(ErrorCode.AUTH_MEMBER_FAIL));
        if (!member.checkPassword(password)) {
            throw new AuthenticationException(ErrorCode.AUTH_MEMBER_FAIL);
        }
    }
}
