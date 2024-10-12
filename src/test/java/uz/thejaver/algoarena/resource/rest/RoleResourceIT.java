package uz.thejaver.algoarena.resource.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import uz.thejaver.algoarena.AbsAlgoArenaTest;
import uz.thejaver.algoarena.domain.Role;
import uz.thejaver.algoarena.domain.enums.Permission;
import uz.thejaver.algoarena.dto.RoleDto;
import uz.thejaver.algoarena.repository.RoleRepository;
import uz.thejaver.algoarena.util.TestUtil;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleResourceIT extends AbsAlgoArenaTest {

    private static final String DEFAULT_NAME = "USER";
    private static final String UPDATED_NAME = "ADMIN";

    private static final Permission DEFAULT_PERMISSION = Permission.CAN_EDIT_PERMISSIONS;
    private static final Permission UPDATED_PERMISSION = Permission.CAN_VIEW_PERMISSIONS;

    private static final String DEFAULT_PREFIX_URL = "/api/roles";
    private static final String ENTITY_API_URL = DEFAULT_PREFIX_URL + "/{id}";

    @Autowired
    private RoleRepository roleRepository;

    private RoleDto roleDto;
    private Role role;

    @BeforeEach
    public void init() {
        initRole();
    }

    @Test
    public void createRoleWithNullName() throws Exception {
        roleDto.setName(null);
        long before = roleRepository.count();
        mvc.perform(post(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(roleDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.attributes.name").isNotEmpty())
        ;
        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    public void createRoleWithBlankName() throws Exception {
        roleDto.setName("");
        long before = roleRepository.count();
        mvc.perform(post(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(roleDto))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.attributes.name").isNotEmpty())
        ;
        long after = roleRepository.count();
        assertThat(after).isEqualTo(before);
    }

    @Test
    public void createRole() throws Exception {
        long before = roleRepository.count();
        mvc.perform(post(DEFAULT_PREFIX_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(roleDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
        ;
        long after = roleRepository.count();
        assertThat(after).isEqualTo(before + 1);
    }


    public void initRole() {
        roleDto = buildDefaultRoleDto();
        role = buildDefaultRole();
    }

    public RoleDto buildDefaultRoleDto() {
        return new RoleDto()
                .setName(DEFAULT_NAME)
                .setPermissions(Set.of(DEFAULT_PERMISSION))
                ;
    }

    public RoleDto buildUpdateRoleDto() {
        return new RoleDto()
                .setName(DEFAULT_NAME)
                .setPermissions(Set.of(UPDATED_PERMISSION));
    }

    public Role buildDefaultRole() {
        return new Role()
                .setName(DEFAULT_NAME)
                .setPermissions(Set.of(DEFAULT_PERMISSION));
    }

    public Role buildUpdatetRole() {
        return new Role()
                .setName(DEFAULT_NAME)
                .setPermissions(Set.of(UPDATED_PERMISSION));
    }

}
