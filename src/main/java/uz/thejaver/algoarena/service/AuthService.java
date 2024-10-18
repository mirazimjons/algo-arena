package uz.thejaver.algoarena.service;

import uz.thejaver.algoarena.dto.AccessTokenRequestDto;
import uz.thejaver.algoarena.dto.AuthResponseDto;
import uz.thejaver.algoarena.dto.RefreshTokenRequestDto;

public abstract class AuthService {

    public abstract AuthResponseDto signIn(AccessTokenRequestDto accessTokenRequestDto);

    public abstract AuthResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);

}
