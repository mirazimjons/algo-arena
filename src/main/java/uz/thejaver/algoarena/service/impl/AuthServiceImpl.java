package uz.thejaver.algoarena.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.thejaver.algoarena.config.security.UserDetailsImpl;
import uz.thejaver.algoarena.config.security.jwt.JwtService;
import uz.thejaver.algoarena.domain.User;
import uz.thejaver.algoarena.dto.AccessTokenRequestDto;
import uz.thejaver.algoarena.dto.AuthResponseDto;
import uz.thejaver.algoarena.exeption.ExceptionType;
import uz.thejaver.algoarena.repository.UserRepository;
import uz.thejaver.algoarena.service.AuthService;
import uz.thejaver.springbootstarterexceptionsupporter.exception.CommonException;
import uz.thejaver.springbootstarterexceptionsupporter.exception.exceptipTypes.DefaultType;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl extends AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDto signIn(@NonNull AccessTokenRequestDto accessTokenRequestDto) {
        User user = userRepository.findByUsername(accessTokenRequestDto.getUsername())
                .orElseThrow(() -> new CommonException(ExceptionType.INVALID_CREDENTIALS, "User not found"));
        if (!passwordEncoder.matches(accessTokenRequestDto.getPassword(), user.getPassword())) {
            throw new CommonException(ExceptionType.INVALID_CREDENTIALS, "Invalid credentials");
        }

        UserDetails userDetails = new UserDetailsImpl(user);
        return new AuthResponseDto()
                .setAccessToken(jwtService.generateToken(userDetails))
                .setRefreshToken(jwtService.generateRefreshToken(userDetails));
    }

}
