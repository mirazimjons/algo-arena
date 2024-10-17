package uz.thejaver.algoarena.config.securityConfig;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class SecurityConstants {

    public static final String[] WHITE_LIST = {
            "/api/auth/**"
    };
    public static final String JWT_HEADER = AUTHORIZATION;
    public static final String TOKEN_PREFIX = "Bearer ";


}
