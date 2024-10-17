package uz.thejaver.algoarena.service;

import uz.thejaver.algoarena.dto.PermissionDto;

import java.util.Optional;
import java.util.Set;

public abstract class PermissionService {

    public abstract Optional<PermissionDto> findByName(String name);

    public abstract Set<PermissionDto> findAll();

    public abstract Long count();

}
