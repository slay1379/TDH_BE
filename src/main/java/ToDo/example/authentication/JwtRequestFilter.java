package ToDo.example.authentication;

import ToDo.example.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private TokenBlacklistService tokenBlacklistService;

    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

            // 블랙리스트에 있는 토큰인지 확인
            if (jwt != null && tokenBlacklistService.isBlacklisted(jwt)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 블랙리스트에 포함되었습니다.");
                return;
            }

            // JWT 검증 로직
            if (jwtUtil.validateToken(jwt)) {
                // JWT에서 사용자 이름과 권한 정보 추출
                String username = jwtUtil.extractUsername(jwt);
                List<GrantedAuthority> authorities = jwtUtil.extractAuthorities(jwt);

                // 바로 인증 객체 생성
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                // SecurityContext에 인증 객체 설정
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
