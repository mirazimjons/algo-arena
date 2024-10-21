package uz.thejaver.algoarena.service.impl;

import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.thejaver.algoarena.config.security.UserDetailsImpl;
import uz.thejaver.algoarena.config.security.jwt.JwtService;
import uz.thejaver.algoarena.domain.User;
import uz.thejaver.algoarena.dto.AccessTokenRequestDto;
import uz.thejaver.algoarena.dto.AuthResponseDto;
import uz.thejaver.algoarena.dto.RefreshTokenRequestDto;
import uz.thejaver.algoarena.dto.SignUpRequestDto;
import uz.thejaver.algoarena.exeption.ExceptionType;
import uz.thejaver.algoarena.mapper.UserMapper;
import uz.thejaver.algoarena.repository.UserRepository;
import uz.thejaver.algoarena.service.AuthService;
import uz.thejaver.springbootstarterexceptionsupporter.exception.CommonException;
import uz.thejaver.springbootstarterexceptionsupporter.exception.exceptipTypes.DefaultType;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl extends AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @NonNull
    public AuthResponseDto signIn(@NonNull AccessTokenRequestDto accessTokenRequestDto) {
        User user = userRepository.findByUsername(accessTokenRequestDto.getUsername())
                .orElseThrow(() -> new CommonException(ExceptionType.INVALID_CREDENTIALS, "User not found"));
        if (!passwordEncoder.matches(accessTokenRequestDto.getPassword(), user.getPassword())) {
            throw new CommonException(ExceptionType.INVALID_CREDENTIALS, "Invalid credentials");
        }

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        return new AuthResponseDto()
                .setAccessToken(jwtService.generateToken(userDetails))
                .setRefreshToken(jwtService.generateRefreshToken(userDetails));
    }

    @Override
    @NonNull
    public AuthResponseDto signUp(@Nonnull SignUpRequestDto signUpRequestDto) {
        userRepository.findByUsername(signUpRequestDto.getUsername())
                .ifPresent(user -> {
                    throw new CommonException(DefaultType.CONFLICT, "User already exists with username: %s".formatted(signUpRequestDto.getUsername()));
                });
        User user = userMapper.signUpRequestToUser(signUpRequestDto);
        user.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        userRepository.save(user);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        return new AuthResponseDto()
                .setAccessToken(jwtService.generateToken(userDetails))
                .setRefreshToken(jwtService.generateRefreshToken(userDetails));
    }

    @Override
    @NonNull
    public AuthResponseDto refreshToken(@Nonnull RefreshTokenRequestDto refreshTokenRequestDto) {
        if (!jwtService.isTokenValid(refreshTokenRequestDto.getRefreshToken())) {
            throw new CommonException(DefaultType.UNAUTHORIZED, "Token is expired or invalid");
        }
        String userId = jwtService.extractUserId(refreshTokenRequestDto.getRefreshToken());
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new CommonException(ExceptionType.INVALID_CREDENTIALS, "User not found"));
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        return new AuthResponseDto()
                .setAccessToken(jwtService.generateToken(userDetails))
                .setRefreshToken(refreshTokenRequestDto.getRefreshToken());
    }

}
