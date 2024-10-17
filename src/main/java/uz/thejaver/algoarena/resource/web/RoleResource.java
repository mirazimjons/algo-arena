package uz.thejaver.algoarena.resource.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.thejaver.algoarena.domain.criteria.RoleCriteria;
import uz.thejaver.algoarena.dto.RoleDto;
import uz.thejaver.algoarena.service.RoleQueryService;
import uz.thejaver.algoarena.service.RoleService;
import uz.thejaver.springbootstarterexceptionsupporter.exception.CommonException;
import uz.thejaver.springbootstarterexceptionsupporter.exception.exceptipTypes.DefaultType;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleResource {

    private final RoleService roleService;
    private final RoleQueryService roleQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority(T(uz.thejaver.algoarena.domain.enums.Permission).CAN_CREATE_ROLES)")
    public RoleDto create(@RequestBody @Valid RoleDto role) {
        return roleService.save(role);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority(T(uz.thejaver.algoarena.domain.enums.Permission).CAN_UPDATE_ROLES)")
    public RoleDto update(@RequestBody @Valid RoleDto role) {
        return roleService.update(role);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(T(uz.thejaver.algoarena.domain.enums.Permission).CAN_READ_ROLES)")
    public RoleDto findById(@PathVariable("id") UUID id) {
        return roleService.getById(id)
                .orElseThrow(() -> new CommonException(DefaultType.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(uz.thejaver.algoarena.domain.enums.Permission).CAN_READ_ROLES)")
    public Page<RoleDto> find(RoleCriteria criteria, Pageable pageable) {
        return roleQueryService.findByCriteria(criteria, pageable);
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyAuthority(T(uz.thejaver.algoarena.domain.enums.Permission).CAN_READ_ROLES)")
    public Long count(RoleCriteria criteria) {
        return roleQueryService.countByCriteria(criteria);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(T(uz.thejaver.algoarena.domain.enums.Permission).CAN_DELETE_ROLES)")
    public void delete(@PathVariable("id") UUID id) {
        roleService.deleteById(id);
    }

}
