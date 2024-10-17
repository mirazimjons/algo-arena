package uz.thejaver.algoarena.resource.rest;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uz.thejaver.algoarena.AbsAlgoArenaTest;
import uz.thejaver.algoarena.config.securityConfig.WithMockUser;
import uz.thejaver.algoarena.domain.enums.Permission;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResourceIT extends AbsAlgoArenaTest {

    static final String DEFAULT_PREFIX_URL = "/api/permissions";
    static final String ENTITY_API_URL = DEFAULT_PREFIX_URL + "/{name}";
    static final String ENTITY_COUNT_URL = DEFAULT_PREFIX_URL + "/count";

    Permission permission;

    @BeforeEach
    void init() {
        initRole();
    }

    @Test
    void findByName_401() throws Exception {
        mvc.perform(get(ENTITY_API_URL, permission.name()))
                .andExpect(status().isUnauthorized())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
    }

    @Test
    @WithMockUser
    void findByName_403() throws Exception {
        mvc.perform(get(ENTITY_API_URL, permission.name()))
                .andExpect(status().isForbidden())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
    }

    @Test
    @WithMockUser(permissions = Permission.CAN_READ_PERMISSIONS)
    void findByNameWithNonExistingPermission() throws Exception {
        mvc.perform(get(ENTITY_API_URL, "SOMETHING"))
                .andExpect(status().isNotFound())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
    }

    @Test
    @WithMockUser(permissions = Permission.CAN_READ_PERMISSIONS)
    void findByName() throws Exception {
        mvc.perform(get(ENTITY_API_URL, permission.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.name").value(permission.name()))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
    }

    @Test
    void findAll_401() throws Exception {
        mvc.perform(get(DEFAULT_PREFIX_URL))
                .andExpect(status().isUnauthorized())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
    }

    @Test
    @WithMockUser
    void findAll_403() throws Exception {
        mvc.perform(get(DEFAULT_PREFIX_URL))
                .andExpect(status().isForbidden())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
    }

    @Test
    @WithMockUser(permissions = Permission.CAN_READ_PERMISSIONS)
    void findAll() throws Exception {
        mvc.perform(get(DEFAULT_PREFIX_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(Permission.values().length)))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
    }

    @Test
    void count_401() throws Exception {
        mvc.perform(get(ENTITY_COUNT_URL))
                .andExpect(status().isUnauthorized())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
    }

    @Test
    @WithMockUser
    void count_403() throws Exception {
        mvc.perform(get(ENTITY_COUNT_URL))
                .andExpect(status().isForbidden())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
    }

    @Test
    @WithMockUser(permissions = Permission.CAN_READ_PERMISSIONS)
    void count() throws Exception {
        mvc.perform(get(ENTITY_COUNT_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(Permission.values().length))
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
        ;
    }

    private void initRole() {
        permission = buildDefaultPermission();
    }

    public static Permission buildDefaultPermission() {
        return Permission.CAN_CREATE_ROLES;
    }

}
