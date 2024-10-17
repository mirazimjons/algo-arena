package uz.thejaver.algoarena.config.securityConfig;

import jakarta.annotation.Nonnull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class WithMockPermissionSecurityContextFactory implements WithSecurityContextFactory<WithMockUser> {

    @Override
    public SecurityContext createSecurityContext(@Nonnull WithMockUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Collection<GrantedAuthority> authorities = Arrays.stream(customUser.permissions())
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        UserDetails principal = new User(customUser.username(), "password", authorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), authorities);
        context.setAuthentication(auth);

        return context;
    }

}
