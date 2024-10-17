package uz.thejaver.algoarena.config.securityConfig;

import lombok.NonNull;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.thejaver.algoarena.domain.Role;
import uz.thejaver.algoarena.domain.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Accessors(chain = true)
public record UserDetailsImpl(@NonNull User user) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(Role::getPermissions)
                .flatMap(Collection::stream)
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getPassword();
    }

}
