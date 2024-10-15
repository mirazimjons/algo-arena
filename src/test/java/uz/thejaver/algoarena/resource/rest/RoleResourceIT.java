package uz.thejaver.algoarena.resource.rest;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import uz.thejaver.algoarena.AbsAlgoArenaTest;
import uz.thejaver.algoarena.domain.Role;
import uz.thejaver.algoarena.domain.enums.Permission;
import uz.thejaver.algoarena.dto.RoleDto;
import uz.thejaver.algoarena.repository.RoleRepository;
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
public class RoleResourceIT extends AbsAlgoArenaTest {

    static final String DEFAULT_NAME = "USER";
    static final String UPDATED_NAME = "ADMIN";

    static final String DEFAULT_DESCRIPTION = "User role";
    static final String UPDATED_DESCRIPTION = "Admin role";

    static final Permission DEFAULT_PERMISSION = Permission.CAN_EDIT_PERMISSIONS;
    static final Permission UPDATED_PERMISSION = Permission.CAN_VIEW_PERMISSIONS;

    static final String DEFAULT_PREFIX_URL = "/api/roles";
    static final String ENTITY_API_URL = DEFAULT_PREFIX_URL + "/{id}";
    static final String ENTITY_COUNT_URL = DEFAULT_PREFIX_URL + "/count";

    static UUID ID;
    static UUID SIMULATED_ID = UUID.randomUUID();

    @Autowired
    RoleRepository roleRepository;

    RoleDto roleDto;
    Role role;

    @BeforeEach
    void init() {
        initRole();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void createRoleWithInvalidName(String name) throws Exception {
        roleDto.setName(name);
        long before = roleRepository.count();
        mvc.perform(post(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(roleDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.attributes.name").isNotEmpty())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    void createRole() throws Exception {
        long before = roleRepository.count();
        mvc.perform(post(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(roleDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.permissions").isArray())
                .andExpect(jsonPath("$.permissions").value(hasSize(1)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
        long after = roleRepository.count();
        assertThat(after).isEqualTo(before + 1);
    }

    @Test
    void updateRoleWithNullName() throws Exception {
        Role savedRole = roleRepository.save(buildDefaultRole());
        RoleDto updatedRoleDto = buildUpdateRoleDto();
        updatedRoleDto.setName(null);
        updatedRoleDto.setId(savedRole.getId());

        long before = roleRepository.count();

        mvc.perform(put(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(updatedRoleDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.attributes.name").isNotEmpty())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    void updateRoleWithBlankName() throws Exception {
        Role savedRole = roleRepository.save(buildDefaultRole());
        RoleDto updatedRoleDto = buildUpdateRoleDto();
        updatedRoleDto.setName("");
        updatedRoleDto.setId(savedRole.getId());

        long before = roleRepository.count();

        mvc.perform(put(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(updatedRoleDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.attributes.name").isNotEmpty())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    void updateRole() throws Exception {
        Role savedRole = roleRepository.save(buildDefaultRole());

        RoleDto updatedRoleDto = buildUpdateRoleDto();
        updatedRoleDto.setId(savedRole.getId());

        long before = roleRepository.count();

        mvc.perform(put(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(updatedRoleDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(savedRole.getId().toString()))
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.description").value(UPDATED_DESCRIPTION))
                .andExpect(jsonPath("$.permissions").isArray())
                .andExpect(jsonPath("$.permissions").value(hasSize(1)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    void getRoleByIdWithNonExistingId() throws Exception {
        Role savedRole = roleRepository.save(buildDefaultRole());

        long before = roleRepository.count();

        mvc.perform(get(ENTITY_API_URL, UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    void getRoleById() throws Exception {
        Role savedRole = roleRepository.save(buildDefaultRole());

        long before = roleRepository.count();

        mvc.perform(get(ENTITY_API_URL, savedRole.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(savedRole.getId().toString()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.permissions").isArray())
                .andExpect(jsonPath("$.permissions").value(hasSize(1)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    void deleteRoleByNonExistingId() throws Exception {
        Role savedRole = roleRepository.save(buildDefaultRole());

        long before = roleRepository.count();

        mvc.perform(delete(ENTITY_API_URL, UUID.randomUUID()))
                .andExpect(status().isNoContent())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    void deleteRoleById() throws Exception {
        Role savedRole = roleRepository.save(buildDefaultRole());

        long before = roleRepository.count();

        mvc.perform(delete(ENTITY_API_URL, savedRole.getId()))
                .andExpect(status().isNoContent())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;

        long after = roleRepository.count();
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

        shouldBeFound("name.equals=%s".formatted(DEFAULT_NAME));
        shouldNotBeFound("name.equals=%s".formatted(UPDATED_NAME));

        shouldBeFound("name.notEquals=%s".formatted(UPDATED_NAME));
        shouldNotBeFound("name.notEquals=%s".formatted(DEFAULT_NAME));

        shouldBeFound("name.specified=" + true);
        shouldNotBeFound("name.specified=" + false);

        shouldBeFound("name.in=%s,%s".formatted(DEFAULT_NAME, UPDATED_NAME));
        shouldNotBeFound("name.in=%s".formatted(UPDATED_NAME));

        shouldBeFound("name.notIn=%s".formatted(UPDATED_NAME));
        shouldNotBeFound("name.notIn=%s,%s".formatted(DEFAULT_NAME, UPDATED_NAME));

        shouldBeFound("name.contains=%s".formatted(DEFAULT_NAME));
        shouldNotBeFound("name.contains=%s".formatted(UPDATED_NAME));

        shouldBeFound("name.doesNotContains=%s".formatted(UPDATED_NAME));
        shouldNotBeFound("name.doesNotContain=%s".formatted(DEFAULT_NAME));
    }

    @Test
    void filterByDescription() throws Exception {
        prepareData();

        shouldBeFound("description.equals=%s".formatted(DEFAULT_DESCRIPTION));
        shouldNotBeFound("description.equals=%s".formatted(UPDATED_DESCRIPTION));

        shouldBeFound("description.notEquals=%s".formatted(UPDATED_DESCRIPTION));
        shouldNotBeFound("description.notEquals=%s".formatted(DEFAULT_DESCRIPTION));

        shouldBeFound("description.specified=" + true);
        shouldNotBeFound("description.specified=" + false);

        shouldBeFound("description.in=%s,%s".formatted(DEFAULT_DESCRIPTION, UPDATED_DESCRIPTION));
        shouldNotBeFound("description.in=%s".formatted(UPDATED_DESCRIPTION));

        shouldBeFound("description.notIn=%s".formatted(UPDATED_DESCRIPTION));
        shouldNotBeFound("description.notIn=%s,%s".formatted(DEFAULT_DESCRIPTION, UPDATED_DESCRIPTION));

        shouldBeFound("description.contains=%s".formatted(DEFAULT_DESCRIPTION));
        shouldNotBeFound("description.contains=%s".formatted(UPDATED_DESCRIPTION));

        shouldBeFound("description.doesNotContains=%s".formatted(UPDATED_DESCRIPTION));
        shouldNotBeFound("description.doesNotContain=%s".formatted(DEFAULT_DESCRIPTION));
    }

    public void initRole() {
        roleDto = buildDefaultRoleDto();
        role = buildDefaultRole();
    }

    public static RoleDto buildDefaultRoleDto() {
        return new RoleDto()
                .setName(DEFAULT_NAME)
                .setPermissions(new HashSet<>(Set.of(DEFAULT_PERMISSION)))
                .setDescription(DEFAULT_DESCRIPTION)
                ;
    }

    public static RoleDto buildUpdateRoleDto() {
        return new RoleDto()
                .setName(UPDATED_NAME)
                .setPermissions(new HashSet<>(Set.of(UPDATED_PERMISSION)))
                .setDescription(UPDATED_DESCRIPTION)
                ;
    }

    public static Role buildDefaultRole() {
        return new Role()
                .setName(DEFAULT_NAME)
                .setPermissions(new HashSet<>(Set.of(DEFAULT_PERMISSION)))
                .setDescription(DEFAULT_DESCRIPTION)
                ;
    }

    public static Role buildUpdatetRole() {
        return new Role()
                .setName(UPDATED_NAME)
                .setPermissions(new HashSet<>(Set.of(UPDATED_PERMISSION)))
                .setDescription(UPDATED_DESCRIPTION)
                ;
    }

    void prepareData() {
        role = buildDefaultRole();
        roleRepository.save(role);
        ID = role.getId();
    }

    void shouldBeFound(String filter, Integer expectedSize) throws Exception {
        mvc.perform(get("%s?%s".formatted(DEFAULT_PREFIX_URL, filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(expectedSize)))
                .andExpect(jsonPath("$.content.[*].id").value(hasItem(ID.toString())))
                .andExpect(jsonPath("$.content.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.content.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
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
