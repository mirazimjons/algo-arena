package uz.thejaver.algoarena.config.securityConfig;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.thejaver.algoarena.domain.User;
import uz.thejaver.algoarena.exeption.ExceptionType;
import uz.thejaver.algoarena.repository.UserRepository;
import uz.thejaver.springbootstarterexceptionsupporter.exception.CommonException;

import java.util.Objects;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SecurityBeans {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> userOptional = userRepository.findByUsername(username);
            return userOptional
                    .map(user -> {
                        if (Objects.isNull(user.getPassword())) {
                            throw new CommonException(ExceptionType.PASSWORD_NOT_DEFINED);
                        }
                        return new UserDetailsImpl(user);
                    })
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(@NonNull AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
