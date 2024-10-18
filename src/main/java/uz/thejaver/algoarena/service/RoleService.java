package uz.thejaver.algoarena.service;

import uz.thejaver.algoarena.domain.Role;
import uz.thejaver.algoarena.dto.RoleDto;
import uz.thejaver.algoarena.mapper.RoleMapper;
import uz.thejaver.algoarena.repository.RoleRepository;
import uz.thejaver.springbootstarterjpasupporter.service.AbsCrudService;

import java.util.UUID;

public abstract class RoleService extends AbsCrudService<UUID, Role, RoleDto> {
    protected RoleService(RoleMapper mapper, RoleRepository repository) {
        super(mapper, repository);
    }
}
