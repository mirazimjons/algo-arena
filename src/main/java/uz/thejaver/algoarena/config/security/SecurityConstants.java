package uz.thejaver.algoarena.config.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {

    protected static final String[] WHITE_LIST = {
            "/api/auth/**"
    };
    public static final String JWT_HEADER = AUTHORIZATION;
    public static final String TOKEN_PREFIX = "Bearer ";


}
