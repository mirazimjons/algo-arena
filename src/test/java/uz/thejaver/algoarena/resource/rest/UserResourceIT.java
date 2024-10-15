package uz.thejaver.algoarena.resource.rest;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    static final RoleDto DEFAULT_ROLE = new RoleDto()
            .setName("ADMIN");
    static final RoleDto UPDATED_ROLE = new RoleDto()
            .setName("USER");

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

    @ParameterizedTest
    @NullAndEmptySource
    void createUserWithInvalidPassword(String password) throws Exception {
        userDto.setPassword(password);
        long before = roleRepository.count();
        mvc.perform(post(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(userDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.attributes.password").isNotEmpty())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

//    @Test
//    void createRole() throws Exception {
//        long before = roleRepository.count();
//        mvc.perform(post(DEFAULT_PREFIX_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(userDto))
//                )
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$").isNotEmpty())
//                .andExpect(jsonPath("$.id").isNotEmpty())
//                .andExpect(jsonPath("$.name").value(DEFAULT_USERNAME))
//                .andExpect(jsonPath("$.description").value(DEFAULT_EMAIL))
//                .andExpect(jsonPath("$.permissions").isArray())
//                .andExpect(jsonPath("$.permissions").value(hasSize(1)))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
//        ;
//        long after = roleRepository.count();
//        assertThat(after).isEqualTo(before + 1);
//    }

//    @Test
//    void updateRoleWithNullName() throws Exception {
//        Role savedRole = roleRepository.save(buildDefaultUser());
//        RoleDto updatedRoleDto = buildUpdateUserDto();
//        updatedRoleDto.setName(null);
//        updatedRoleDto.setId(savedRole.getId());
//
//        long before = roleRepository.count();
//
//        mvc.perform(put(DEFAULT_PREFIX_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(updatedRoleDto))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$").isNotEmpty())
//                .andExpect(jsonPath("$.title").isNotEmpty())
//                .andExpect(jsonPath("$.attributes.name").isNotEmpty())
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
//        ;
//
//        long after = roleRepository.count();
//        assertThat(after).isEqualTo(before);
//    }
//
//    @Test
//    void updateRoleWithBlankName() throws Exception {
//        Role savedRole = roleRepository.save(buildDefaultUser());
//        RoleDto updatedRoleDto = buildUpdateUserDto();
//        updatedRoleDto.setName("");
//        updatedRoleDto.setId(savedRole.getId());
//
//        long before = roleRepository.count();
//
//        mvc.perform(put(DEFAULT_PREFIX_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(updatedRoleDto))
//                )
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$").isNotEmpty())
//                .andExpect(jsonPath("$.title").isNotEmpty())
//                .andExpect(jsonPath("$.attributes.name").isNotEmpty())
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
//        ;
//
//        long after = roleRepository.count();
//        assertThat(after).isEqualTo(before);
//    }
//
//    @Test
//    void updateRole() throws Exception {
//        Role savedRole = roleRepository.save(buildDefaultUser());
//
//        RoleDto updatedRoleDto = buildUpdateUserDto();
//        updatedRoleDto.setId(savedRole.getId());
//
//        long before = roleRepository.count();
//
//        mvc.perform(put(DEFAULT_PREFIX_URL)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(TestUtil.convertObjectToJsonBytes(updatedRoleDto))
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isNotEmpty())
//                .andExpect(jsonPath("$.id").value(savedRole.getId().toString()))
//                .andExpect(jsonPath("$.name").value(UPDATED_USERNAME))
//                .andExpect(jsonPath("$.description").value(UPDATED_EMAIL))
//                .andExpect(jsonPath("$.permissions").isArray())
//                .andExpect(jsonPath("$.permissions").value(hasSize(1)))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
//        ;
//
//        long after = roleRepository.count();
//        assertThat(after).isEqualTo(before);
//    }
//
//    @Test
//    void getRoleByIdWithNonExistingId() throws Exception {
//        Role savedRole = roleRepository.save(buildDefaultUser());
//
//        long before = roleRepository.count();
//
//        mvc.perform(get(ENTITY_API_URL, UUID.randomUUID()))
//                .andExpect(status().isNotFound())
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
//        ;
//
//        long after = roleRepository.count();
//        assertThat(after).isEqualTo(before);
//    }
//
//    @Test
//    void getRoleById() throws Exception {
//        Role savedRole = roleRepository.save(buildDefaultUser());
//
//        long before = roleRepository.count();
//
//        mvc.perform(get(ENTITY_API_URL, savedRole.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isNotEmpty())
//                .andExpect(jsonPath("$.id").value(savedRole.getId().toString()))
//                .andExpect(jsonPath("$.name").value(DEFAULT_USERNAME))
//                .andExpect(jsonPath("$.description").value(DEFAULT_EMAIL))
//                .andExpect(jsonPath("$.permissions").isArray())
//                .andExpect(jsonPath("$.permissions").value(hasSize(1)))
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
//        ;
//
//        long after = roleRepository.count();
//        assertThat(after).isEqualTo(before);
//    }
//
//    @Test
//    void deleteRoleByNonExistingId() throws Exception {
//        Role savedRole = roleRepository.save(buildDefaultUser());
//
//        long before = roleRepository.count();
//
//        mvc.perform(delete(ENTITY_API_URL, UUID.randomUUID()))
//                .andExpect(status().isNoContent())
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
//        ;
//
//        long after = roleRepository.count();
//        assertThat(after).isEqualTo(before);
//    }
//
//    @Test
//    void deleteRoleById() throws Exception {
//        Role savedRole = roleRepository.save(buildDefaultUser());
//
//        long before = roleRepository.count();
//
//        mvc.perform(delete(ENTITY_API_URL, savedRole.getId()))
//                .andExpect(status().isNoContent())
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
//        ;
//
//        long after = roleRepository.count();
//        assertThat(after).isEqualTo(before - 1);
//    }
//
//    @Test
//    void filterById() throws Exception {
//        prepareData();
//
//        shouldBeFound("id.equals=%s".formatted(ID));
//        shouldNotBeFound("id.equals=%s".formatted(SIMULATED_ID));
//
//        shouldBeFound("id.notEquals=%s".formatted(SIMULATED_ID));
//        shouldNotBeFound("id.notEquals=%s".formatted(ID));
//
//        shouldBeFound("id.specified=" + true);
//        shouldNotBeFound("id.specified=" + false);
//
//        shouldBeFound("id.in=%s,%s".formatted(ID, SIMULATED_ID));
//        shouldNotBeFound("id.in=%s".formatted(SIMULATED_ID));
//
//        shouldBeFound("id.notIn=%s".formatted(SIMULATED_ID));
//        shouldNotBeFound("id.notIn=%s,%s".formatted(ID, SIMULATED_ID));
//    }
//
//    @Test
//    void filterByName() throws Exception {
//        prepareData();
//
//        shouldBeFound("name.equals=%s".formatted(DEFAULT_USERNAME));
//        shouldNotBeFound("name.equals=%s".formatted(UPDATED_USERNAME));
//
//        shouldBeFound("name.notEquals=%s".formatted(UPDATED_USERNAME));
//        shouldNotBeFound("name.notEquals=%s".formatted(DEFAULT_USERNAME));
//
//        shouldBeFound("name.specified=" + true);
//        shouldNotBeFound("name.specified=" + false);
//
//        shouldBeFound("name.in=%s,%s".formatted(DEFAULT_USERNAME, UPDATED_USERNAME));
//        shouldNotBeFound("name.in=%s".formatted(UPDATED_USERNAME));
//
//        shouldBeFound("name.notIn=%s".formatted(UPDATED_USERNAME));
//        shouldNotBeFound("name.notIn=%s,%s".formatted(DEFAULT_USERNAME, UPDATED_USERNAME));
//
//        shouldBeFound("name.contains=%s".formatted(DEFAULT_USERNAME));
//        shouldNotBeFound("name.contains=%s".formatted(UPDATED_USERNAME));
//
//        shouldBeFound("name.doesNotContains=%s".formatted(UPDATED_USERNAME));
//        shouldNotBeFound("name.doesNotContain=%s".formatted(DEFAULT_USERNAME));
//    }
//
//    @Test
//    void filterByDescription() throws Exception {
//        prepareData();
//
//        shouldBeFound("description.equals=%s".formatted(DEFAULT_EMAIL));
//        shouldNotBeFound("description.equals=%s".formatted(UPDATED_EMAIL));
//
//        shouldBeFound("description.notEquals=%s".formatted(UPDATED_EMAIL));
//        shouldNotBeFound("description.notEquals=%s".formatted(DEFAULT_EMAIL));
//
//        shouldBeFound("description.specified=" + true);
//        shouldNotBeFound("description.specified=" + false);
//
//        shouldBeFound("description.in=%s,%s".formatted(DEFAULT_EMAIL, UPDATED_EMAIL));
//        shouldNotBeFound("description.in=%s".formatted(UPDATED_EMAIL));
//
//        shouldBeFound("description.notIn=%s".formatted(UPDATED_EMAIL));
//        shouldNotBeFound("description.notIn=%s,%s".formatted(DEFAULT_EMAIL, UPDATED_EMAIL));
//
//        shouldBeFound("description.contains=%s".formatted(DEFAULT_EMAIL));
//        shouldNotBeFound("description.contains=%s".formatted(UPDATED_EMAIL));
//
//        shouldBeFound("description.doesNotContains=%s".formatted(UPDATED_EMAIL));
//        shouldNotBeFound("description.doesNotContain=%s".formatted(DEFAULT_EMAIL));
//    }

    public void initUser() {
        userDto = buildDefaultUserDto();
        user = buildDefaultUser();
    }

    public UserDto buildDefaultUserDto() {
        return new UserDto()
                .setUsername(DEFAULT_USERNAME)
                .setEmail(DEFAULT_EMAIL)
                .setPassword(DEFAULT_PASSWORD)
                .setRoles(new HashSet<>(Set.of(DEFAULT_ROLE)))
                ;
    }

    public UserDto buildUpdateUserDto() {
        return new UserDto()
                .setUsername(UPDATED_USERNAME)
                .setEmail(UPDATED_EMAIL)
                .setPassword(UPDATED_PASSWORD)
                .setRoles(new HashSet<>(Set.of(UPDATED_ROLE)))
                ;
    }

    public User buildDefaultUser() {
        Role role = new Role().setName("ADMIN");
        roleRepository.save(role);
        return new User()
                .setUsername(DEFAULT_USERNAME)
                .setEmail(DEFAULT_EMAIL)
                .setPassword(DEFAULT_PASSWORD)
                .setRoles(new HashSet<>(Set.of(role)))
                ;
    }

    public User buildUpdatedUser() {
        Role role = new Role().setName("USER");
        roleRepository.save(role);
        return new User()
                .setUsername(UPDATED_USERNAME)
                .setEmail(UPDATED_EMAIL)
                .setPassword(UPDATED_PASSWORD)
                .setRoles(new HashSet<>(Set.of(role)))
                ;
    }

    void prepareData() {
        user = buildDefaultUser();
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
                .andExpect(jsonPath("$.content.[*].name").value(hasItem(DEFAULT_USERNAME)))
                .andExpect(jsonPath("$.content.[*].description").value(hasItem(DEFAULT_EMAIL)))
                .andExpect(jsonPath("$.content.[*].permissions").isArray())
                .andExpect(jsonPath("$.content.[*].permissions").value(hasSize(1)))
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
