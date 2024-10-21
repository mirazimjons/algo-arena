package uz.thejaver.algoarena.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.thejaver.algoarena.config.security.SecurityConstants;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(SecurityConstants.JWT_HEADER);
        if (Objects.isNull(authHeader) || !authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            doFilter(request, response, filterChain);
            return;
        }

        if (Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
            doFilter(request, response, filterChain);
            return;
        }

        final String jwt = authHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
        if (!jwtService.isTokenValid(jwt)) {
            doFilter(request, response, filterChain);
            return;
        }

        final String userId = jwtService.extractUserId(jwt);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        doFilter(request, response, filterChain);
    }

    private void doFilter(@NonNull HttpServletRequest request,
                          @NonNull HttpServletResponse response,
                          @NonNull FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);
    }

}
