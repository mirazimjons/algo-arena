package uz.thejaver.algoarena.config.security;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class SecurityUtil {

    public static Optional<UUID> getUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> (UserDetailsImpl) authentication.getPrincipal())
                .map(UserDetailsImpl::getId);
    }

    public static Optional<String> getUserIdAsString() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> (UserDetailsImpl) authentication.getPrincipal())
                .map(UserDetailsImpl::getId)
                .map(UUID::toString);
    }

}
