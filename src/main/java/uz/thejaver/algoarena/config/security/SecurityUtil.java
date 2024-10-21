package uz.thejaver.algoarena.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SecurityUtil {

    public static Optional<UUID> getUserId() {
        return getUserDetails().map(UserDetailsImpl::getId);
    }

    public static Optional<String> getUserIdAsString() {
        return getUserId().map(UUID::toString);
    }

    public static Optional<UserDetailsImpl> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (Objects.isNull(principal)) {
            return Optional.empty();
        }

        if (principal instanceof UserDetailsImpl userDetails) {
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }

}
