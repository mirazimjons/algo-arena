package uz.thejaver.algoarena.resource.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public RoleDto create(@RequestBody @Valid RoleDto role) {
        return roleService.save(role);
    }

    @PutMapping
    public RoleDto update(@RequestBody @Valid RoleDto role) {
        return roleService.update(role);
    }

    @GetMapping("/{id}")
    public RoleDto findById(@PathVariable("id") UUID id) {
        return roleService.getById(id)
                .orElseThrow(() -> new CommonException(DefaultType.NOT_FOUND));
    }

    @GetMapping
    public Page<RoleDto> find(RoleCriteria criteria, Pageable pageable) {
        return roleQueryService.findByCriteria(criteria, pageable);
    }

    @GetMapping("/count")
    public Long count(RoleCriteria criteria) {
        return roleQueryService.countByCriteria(criteria);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id) {
        roleService.deleteById(id);
    }

}
