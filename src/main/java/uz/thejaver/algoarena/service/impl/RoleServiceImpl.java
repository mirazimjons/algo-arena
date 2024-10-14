package uz.thejaver.algoarena.service.impl;

import org.springframework.stereotype.Service;
import uz.thejaver.algoarena.mapper.RoleMapper;
import uz.thejaver.algoarena.repository.RoleRepository;
import uz.thejaver.algoarena.service.RoleService;

@Service
public class RoleServiceImpl extends RoleService {
    public RoleServiceImpl(RoleMapper mapper, RoleRepository repository) {
        super(mapper, repository);
    }
}
