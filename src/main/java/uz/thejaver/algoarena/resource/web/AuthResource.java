package uz.thejaver.algoarena.resource.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.thejaver.algoarena.dto.AccessTokenRequestDto;
import uz.thejaver.algoarena.dto.AuthResponseDto;
import uz.thejaver.algoarena.dto.RefreshTokenRequestDto;
import uz.thejaver.algoarena.service.AuthService;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthResource {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public AuthResponseDto signIn(@RequestBody @Valid AccessTokenRequestDto accessTokenRequestDto) {
        return authService.signIn(accessTokenRequestDto);
    }

    @PostMapping("/refresh-token")
    public AuthResponseDto refreshToken(@RequestBody @Valid RefreshTokenRequestDto refreshTokenRequestDto) {
        return authService.refreshToken(refreshTokenRequestDto);
    }

}
