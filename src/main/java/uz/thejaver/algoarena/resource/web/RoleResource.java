package uz.thejaver.algoarena.resource.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.thejaver.algoarena.dto.RoleDto;
import uz.thejaver.algoarena.service.RoleService;

@Slf4j
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleResource {

    private final RoleService roleService;

    @PostMapping
    public RoleDto create(@RequestBody @Valid RoleDto role) {
        return roleService.save(role);
    }

}
