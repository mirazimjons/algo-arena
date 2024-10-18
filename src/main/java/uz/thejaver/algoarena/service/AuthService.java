package uz.thejaver.algoarena.service;

import uz.thejaver.algoarena.dto.AccessTokenRequestDto;
import uz.thejaver.algoarena.dto.AuthResponseDto;

public abstract class AuthService {

    public abstract AuthResponseDto signIn(AccessTokenRequestDto accessTokenRequestDto);

}
