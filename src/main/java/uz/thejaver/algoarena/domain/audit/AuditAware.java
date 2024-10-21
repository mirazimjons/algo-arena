package uz.thejaver.algoarena.domain.audit;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import uz.thejaver.algoarena.config.security.SecurityConstants;
import uz.thejaver.algoarena.config.security.SecurityUtil;

import java.util.Optional;

@Component
public class AuditAware implements AuditorAware<String> {

    @Nonnull
    @Override
    public Optional<String> getCurrentAuditor() {
        return SecurityUtil.getUserIdAsString()
                .or(() -> Optional.of(SecurityConstants.SYSTEM));
    }

}
