package uz.thejaver.algoarena.resource.rest;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.thejaver.algoarena.AbsAlgoArenaTest;
import uz.thejaver.algoarena.domain.User;
import uz.thejaver.algoarena.dto.AccessTokenRequestDto;
import uz.thejaver.algoarena.repository.UserRepository;
import uz.thejaver.algoarena.util.TestUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResourceIT extends AbsAlgoArenaTest {

    static final String DEFAULT_USERNAME = "john";
    static final String NON_EXISTING_USERNAME = "doe";

    static final String DEFAULT_PASSWORD = "password";
    static final String WRONG_PASSWORD = "noPassword";

    static final String DEFAULT_PREFIX_URL = "/api/auth";
    static final String SIGN_IN_URL = DEFAULT_PREFIX_URL + "/sign-in";

    AccessTokenRequestDto accessTokenRequestDto;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        init();
    }

    @Test
    void testSignInWithNonExistingUser() throws Exception {
        userRepository.save(
                new User()
                        .setUsername(NON_EXISTING_USERNAME)
                        .setPassword(passwordEncoder.encode(DEFAULT_PASSWORD))
        );
        mvc.perform(post(SIGN_IN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(accessTokenRequestDto))
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Test
    void testSignInWithWrongPassword() throws Exception {
        userRepository.save(
                new User()
                        .setUsername(DEFAULT_USERNAME)
                        .setPassword(passwordEncoder.encode(DEFAULT_PASSWORD))
        );
        accessTokenRequestDto.setPassword(WRONG_PASSWORD);
        mvc.perform(post(SIGN_IN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(accessTokenRequestDto))
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Test
    void testSignIn() throws Exception {
        userRepository.save(
                new User()
                        .setUsername(DEFAULT_USERNAME)
                        .setPassword(passwordEncoder.encode(DEFAULT_PASSWORD))
        );
        mvc.perform(post(SIGN_IN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(accessTokenRequestDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    private void init() {
        accessTokenRequestDto = buildDefaultAccessTokenRequestDto();
    }

    public static AccessTokenRequestDto buildDefaultAccessTokenRequestDto() {
        return new AccessTokenRequestDto()
                .setUsername(DEFAULT_USERNAME)
                .setPassword(DEFAULT_PASSWORD);
    }

}
