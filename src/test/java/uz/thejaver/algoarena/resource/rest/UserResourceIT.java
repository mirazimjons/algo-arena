package uz.thejaver.algoarena.resource.rest;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import uz.thejaver.algoarena.AbsAlgoArenaTest;
import uz.thejaver.algoarena.domain.Role;
import uz.thejaver.algoarena.domain.User;
import uz.thejaver.algoarena.dto.RoleDto;
import uz.thejaver.algoarena.dto.UserDto;
import uz.thejaver.algoarena.repository.RoleRepository;
import uz.thejaver.algoarena.repository.UserRepository;
import uz.thejaver.algoarena.util.TestUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResourceIT extends AbsAlgoArenaTest {

    static final String DEFAULT_USERNAME = "Jack";
    static final String UPDATED_USERNAME = "Bob";

    static final String DEFAULT_EMAIL = "someone@localhost.uz";
    static final String UPDATED_EMAIL = "another.one@localhost.uz";

    static final String DEFAULT_PASSWORD = "123";
    static final String UPDATED_PASSWORD = "abc";

    static final RoleDto DEFAULT_ROLE = RoleResourceIT.buildDefaultRoleDto();
    static final RoleDto UPDATED_ROLE = RoleResourceIT.buildUpdateRoleDto();

    static final String DEFAULT_PREFIX_URL = "/api/users";
    static final String ENTITY_API_URL = DEFAULT_PREFIX_URL + "/{id}";
    static final String ENTITY_COUNT_URL = DEFAULT_PREFIX_URL + "/count";

    static UUID ID;
    static UUID SIMULATED_ID = UUID.randomUUID();

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    UserDto userDto;
    User user;

    @BeforeEach
    void init() {
        initUser();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "#someone#"})
    @NullAndEmptySource
    void createUserWithInvalidUsername(String username) throws Exception {
        userDto.setUsername(username);
        long before = roleRepository.count();
        mvc.perform(post(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.attributes.username").isNotEmpty())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "ab.localhost"})
    @NullAndEmptySource
    void createUserWithInvalidEmail(String email) throws Exception {
        userDto.setEmail(email);
        long before = roleRepository.count();
        mvc.perform(post(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.attributes.email").isNotEmpty())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    void createUser() throws Exception {
        long rolesBefore = roleRepository.count();
        long usersBefore = userRepository.count();
        mvc.perform(post(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.password").isEmpty())
                .andExpect(jsonPath("$.roles").value(hasSize(1)))
                .andExpect(jsonPath("$.roles.[0].id").isNotEmpty())
                .andExpect(jsonPath("$.roles.[0].id").value(DEFAULT_ROLE.getId().toString()))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
        long rolesAfter = roleRepository.count();
        long usersAfter = userRepository.count();
        assertThat(rolesAfter).isEqualTo(rolesBefore);
        assertThat(usersAfter).isEqualTo(usersBefore + 1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "#someone#"})
    @NullAndEmptySource
    void updateUserWithInvalidUsername(String username) throws Exception {
        userRepository.save(user);
        userDto = buildUpdateUserDto()
                .setUsername(username);
        userDto.setId(user.getId());
        long before = roleRepository.count();

        mvc.perform(put(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.attributes.username").isNotEmpty())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ab", "ab.localhost"})
    @NullAndEmptySource
    void updateUserWithInvalidEmail(String email) throws Exception {
        userRepository.save(user);
        userDto = buildUpdateUserDto()
                .setEmail(email);
        userDto.setId(user.getId());
        long before = roleRepository.count();

        mvc.perform(put(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.attributes.email").isNotEmpty())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    void updateUser() throws Exception {
        userRepository.save(user);
        userDto = buildUpdateUserDto();
        userDto.setId(user.getId());
        userDto.setRoles(new HashSet<>(Set.of(UPDATED_ROLE)));

        long rolesBefore = roleRepository.count();
        long usersBefore = userRepository.count();
        mvc.perform(put(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value(UPDATED_USERNAME))
                .andExpect(jsonPath("$.email").value(UPDATED_EMAIL))
                .andExpect(jsonPath("$.password").isEmpty())
                .andExpect(jsonPath("$.roles").value(hasSize(1)))
                .andExpect(jsonPath("$.roles.[0].id").isNotEmpty())
                .andExpect(jsonPath("$.roles.[0].id").value(UPDATED_ROLE.getId().toString()))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
        long rolesAfter = roleRepository.count();
        long usersAfter = userRepository.count();
        assertThat(rolesAfter).isEqualTo(rolesBefore);
        assertThat(usersAfter).isEqualTo(usersBefore);
    }

    @Test
    void getUserByIdWithNonExistingId() throws Exception {
        userRepository.save(user);
        long before = userRepository.count();

        mvc.perform(get(ENTITY_API_URL, UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        long after = userRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    void getRoleById() throws Exception {
        userRepository.save(user);

        long before = userRepository.count();

        mvc.perform(get(ENTITY_API_URL, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.password").isEmpty())
                .andExpect(jsonPath("$.roles").value(hasSize(1)))
                .andExpect(jsonPath("$.roles.[0].id").isNotEmpty())
                .andExpect(jsonPath("$.roles.[0].id").value(DEFAULT_ROLE.getId().toString()))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        long after = userRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    void deleteUserByNonExistingId() throws Exception {
        userRepository.save(buildDefaultUser());

        long before = userRepository.count();

        mvc.perform(delete(ENTITY_API_URL, UUID.randomUUID()))
                .andExpect(status().isNoContent())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        long after = userRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    void deleteRoleById() throws Exception {
        userRepository.save(user);

        long before = userRepository.count();

        mvc.perform(delete(ENTITY_API_URL, user.getId()))
                .andExpect(status().isNoContent())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        long after = userRepository.count();
        assertThat(after).isEqualTo(before - 1);
    }

    @Test
    void filterById() throws Exception {
        prepareData();

        shouldBeFound("id.equals=%s".formatted(ID));
        shouldNotBeFound("id.equals=%s".formatted(SIMULATED_ID));

        shouldBeFound("id.notEquals=%s".formatted(SIMULATED_ID));
        shouldNotBeFound("id.notEquals=%s".formatted(ID));

        shouldBeFound("id.specified=" + true);
        shouldNotBeFound("id.specified=" + false);

        shouldBeFound("id.in=%s,%s".formatted(ID, SIMULATED_ID));
        shouldNotBeFound("id.in=%s".formatted(SIMULATED_ID));

        shouldBeFound("id.notIn=%s".formatted(SIMULATED_ID));
        shouldNotBeFound("id.notIn=%s,%s".formatted(ID, SIMULATED_ID));
    }

    @Test
    void filterByName() throws Exception {
        prepareData();

        shouldBeFound("username.equals=%s".formatted(DEFAULT_USERNAME));
        shouldNotBeFound("username.equals=%s".formatted(UPDATED_USERNAME));

        shouldBeFound("username.notEquals=%s".formatted(UPDATED_USERNAME));
        shouldNotBeFound("username.notEquals=%s".formatted(DEFAULT_USERNAME));

        shouldBeFound("username.specified=" + true);
        shouldNotBeFound("username.specified=" + false);

        shouldBeFound("username.in=%s,%s".formatted(DEFAULT_USERNAME, UPDATED_USERNAME));
        shouldNotBeFound("username.in=%s".formatted(UPDATED_USERNAME));

        shouldBeFound("username.notIn=%s".formatted(UPDATED_USERNAME));
        shouldNotBeFound("username.notIn=%s,%s".formatted(DEFAULT_USERNAME, UPDATED_USERNAME));

        shouldBeFound("username.contains=%s".formatted(DEFAULT_USERNAME));
        shouldNotBeFound("username.contains=%s".formatted(UPDATED_USERNAME));

        shouldBeFound("username.doesNotContains=%s".formatted(UPDATED_USERNAME));
        shouldNotBeFound("username.doesNotContain=%s".formatted(DEFAULT_USERNAME));
    }

    @Test
    void filterByDescription() throws Exception {
        prepareData();

        shouldBeFound("email.equals=%s".formatted(DEFAULT_EMAIL));
        shouldNotBeFound("email.equals=%s".formatted(UPDATED_EMAIL));

        shouldBeFound("email.notEquals=%s".formatted(UPDATED_EMAIL));
        shouldNotBeFound("email.notEquals=%s".formatted(DEFAULT_EMAIL));

        shouldBeFound("email.specified=" + true);
        shouldNotBeFound("email.specified=" + false);

        shouldBeFound("email.in=%s,%s".formatted(DEFAULT_EMAIL, UPDATED_EMAIL));
        shouldNotBeFound("email.in=%s".formatted(UPDATED_EMAIL));

        shouldBeFound("email.notIn=%s".formatted(UPDATED_EMAIL));
        shouldNotBeFound("email.notIn=%s,%s".formatted(DEFAULT_EMAIL, UPDATED_EMAIL));

        shouldBeFound("email.contains=%s".formatted(DEFAULT_EMAIL));
        shouldNotBeFound("email.contains=%s".formatted(UPDATED_EMAIL));

        shouldBeFound("email.doesNotContains=%s".formatted(UPDATED_EMAIL));
        shouldNotBeFound("email.doesNotContain=%s".formatted(DEFAULT_EMAIL));
    }

    @Test
    void filterByRoleId() throws Exception {
        prepareData();

        shouldBeFound("roleId.equals=%s".formatted(DEFAULT_ROLE.getId()));
        shouldNotBeFound("roleId.equals=%s".formatted(SIMULATED_ID));

        shouldBeFound("roleId.notEquals=%s".formatted(SIMULATED_ID));
        shouldNotBeFound("roleId.notEquals=%s".formatted(DEFAULT_ROLE.getId()));

        shouldBeFound("roleId.specified=" + true);
        shouldNotBeFound("roleId.specified=" + false);

        shouldBeFound("roleId.in=%s,%s".formatted(DEFAULT_ROLE.getId(), SIMULATED_ID));
        shouldNotBeFound("roleId.in=%s".formatted(SIMULATED_ID));

        shouldBeFound("roleId.notIn=%s".formatted(SIMULATED_ID));
        shouldNotBeFound("roleId.notIn=%s,%s".formatted(DEFAULT_ROLE.getId(), SIMULATED_ID));
    }

    private void initUser() {
        initRole();
        userDto = buildDefaultUserDto()
                .setRoles(new HashSet<>(Set.of(DEFAULT_ROLE)));

        Role role = RoleResourceIT.buildDefaultRole();
        role.setId(DEFAULT_ROLE.getId());
        user = buildDefaultUser()
                .setRoles(new HashSet<>(Set.of(role)));
    }

    private void initRole() {
        Role role = RoleResourceIT.buildDefaultRole();
        roleRepository.save(role);
        DEFAULT_ROLE.setId(role.getId());

        Role updatedRole = RoleResourceIT.buildUpdatedRole();
        roleRepository.save(updatedRole);
        UPDATED_ROLE.setId(updatedRole.getId());
    }

    public static UserDto buildDefaultUserDto() {
        return new UserDto()
                .setUsername(DEFAULT_USERNAME)
                .setEmail(DEFAULT_EMAIL)
                .setPassword(DEFAULT_PASSWORD)
                ;
    }

    public static UserDto buildUpdateUserDto() {
        return new UserDto()
                .setUsername(UPDATED_USERNAME)
                .setEmail(UPDATED_EMAIL)
                .setPassword(UPDATED_PASSWORD)
                ;
    }

    public static User buildDefaultUser() {
        return new User()
                .setUsername(DEFAULT_USERNAME)
                .setEmail(DEFAULT_EMAIL)
                .setPassword(DEFAULT_PASSWORD)
                ;
    }

    public static User buildUpdatedUser() {
        return new User()
                .setUsername(UPDATED_USERNAME)
                .setEmail(UPDATED_EMAIL)
                .setPassword(UPDATED_PASSWORD)
                ;
    }

    void prepareData() {
        userRepository.save(user);
        ID = user.getId();
    }

    void shouldBeFound(String filter, Integer expectedSize) throws Exception {
        mvc.perform(get("%s?%s".formatted(DEFAULT_PREFIX_URL, filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(expectedSize)))
                .andExpect(jsonPath("$.content.[*].id").value(hasItem(ID.toString())))
                .andExpect(jsonPath("$.content.[*].username").value(hasItem(DEFAULT_USERNAME)))
                .andExpect(jsonPath("$.content.[*].email").value(hasItem(DEFAULT_EMAIL)))
                .andExpect(jsonPath("$.content.[*].roles").isArray())
                .andExpect(jsonPath("$.content.[*].roles.[0].id").value(DEFAULT_ROLE.getId().toString()))
                .andExpect(jsonPath("$.content.[*].roles.[0].name").value(DEFAULT_ROLE.getName()))
        ;
        mvc.perform(get("%s?%s".formatted(ENTITY_COUNT_URL, filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(expectedSize));
    }

    void shouldNotBeFound(String filter, Integer expectedSize) throws Exception {
        mvc.perform(get("%s?%s".formatted(DEFAULT_PREFIX_URL, filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(expectedSize)))
        ;
        mvc.perform(get("%s?%s".formatted(ENTITY_COUNT_URL, filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(expectedSize));
    }

    void shouldBeFound(String filter) throws Exception {
        shouldBeFound(filter, 1);
    }

    void shouldNotBeFound(String filter) throws Exception {
        shouldNotBeFound(filter, 0);
    }

}
