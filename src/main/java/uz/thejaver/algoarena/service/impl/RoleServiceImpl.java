package uz.thejaver.algoarena.service.impl;

import org.springframework.stereotype.Service;
import uz.thejaver.algoarena.mapper.RoleMapper;
import uz.thejaver.algoarena.repository.RoleRepository;

@Service
public class RoleServiceImpl extends uz.thejaver.algoarena.service.RoleService {
    public RoleServiceImpl(RoleMapper mapper, RoleRepository repository) {
        super(mapper, repository);
    }
}
