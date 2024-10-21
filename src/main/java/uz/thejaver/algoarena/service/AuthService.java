package uz.thejaver.algoarena.service;

import jakarta.annotation.Nonnull;
import lombok.NonNull;
import uz.thejaver.algoarena.dto.AccessTokenRequestDto;
import uz.thejaver.algoarena.dto.AuthResponseDto;
import uz.thejaver.algoarena.dto.RefreshTokenRequestDto;
import uz.thejaver.algoarena.dto.SignUpRequestDto;

public abstract class AuthService {

    @NonNull
    public abstract AuthResponseDto signIn(@NonNull AccessTokenRequestDto accessTokenRequestDto);

    @NonNull
    public abstract AuthResponseDto signUp(@NonNull SignUpRequestDto signUpRequestDto);

    @NonNull
    public abstract AuthResponseDto refreshToken(@Nonnull RefreshTokenRequestDto refreshTokenRequestDto);

}
