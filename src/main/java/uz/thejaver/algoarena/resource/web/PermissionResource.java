package uz.thejaver.algoarena.resource.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.thejaver.algoarena.dto.PermissionDto;
import uz.thejaver.algoarena.service.PermissionService;
import uz.thejaver.springbootstarterexceptionsupporter.exception.CommonException;
import uz.thejaver.springbootstarterexceptionsupporter.exception.exceptipTypes.DefaultType;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionResource {

    private final PermissionService permissionService;

    @GetMapping("/{name}")
    @PreAuthorize("hasAnyAuthority(T(uz.thejaver.algoarena.domain.enums.Permission).CAN_READ_PERMISSIONS)")
    public PermissionDto findById(@PathVariable("name") String name) {
        return permissionService.findByName(name)
                .orElseThrow(() -> new CommonException(DefaultType.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(uz.thejaver.algoarena.domain.enums.Permission).CAN_READ_PERMISSIONS)")
    public Set<PermissionDto> find() {
        return permissionService.findAll();
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyAuthority(T(uz.thejaver.algoarena.domain.enums.Permission).CAN_READ_PERMISSIONS)")
    public Long count() {
        return permissionService.count();
    }

}
