package uz.thejaver.algoarena.config.security;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public interface SecurityConstants {

    String[] WHITE_LIST = {
            "/api/auth/**"
    };
    String SYSTEM = "system";
    String JWT_HEADER = AUTHORIZATION;
    String TOKEN_PREFIX = "Bearer ";
    String ROLES_CLAIM_NAME = "roles";
    String USER_ID_CLAIM_NAME = "userId";

}
