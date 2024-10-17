package uz.thejaver.algoarena.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.thejaver.algoarena.domain.enums.Permission;
import uz.thejaver.algoarena.dto.PermissionDto;
import uz.thejaver.algoarena.mapper.PermissionMapper;
import uz.thejaver.algoarena.service.PermissionService;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends PermissionService {

    private final PermissionMapper permissionMapper;

    @Override
    public Optional<PermissionDto> findByName(String name) {
        return Arrays.stream(Permission.values())
                .filter(permission -> Objects.equals(permission.name(), name))
                .map(permissionMapper::toDto)
                .findFirst();
    }

    @Override
    public Set<PermissionDto> findAll() {
        return Arrays.stream(Permission.values())
                .map(permissionMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Long count() {
        return (long) Permission.values().length;
    }

}
