package uz.thejaver.algoarena.service;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import uz.thejaver.algoarena.domain.Role;
import uz.thejaver.algoarena.domain.Role_;
import uz.thejaver.algoarena.domain.criteria.RoleCriteria;
import uz.thejaver.algoarena.dto.RoleDto;
import uz.thejaver.algoarena.mapper.RoleMapper;
import uz.thejaver.algoarena.repository.RoleRepository;
import uz.thejaver.springbootstarterjpasupporter.service.AbsQueryService;

import java.util.Objects;
import java.util.UUID;

@Service
public class RoleQueryService extends AbsQueryService<UUID, Role, RoleDto, RoleCriteria> {
    public RoleQueryService(RoleRepository repository, RoleMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected Specification<Role> modifySpecification(@NonNull Specification<Role> specification, @NonNull RoleCriteria criteria) {
        SpecificationJoiner<Role> joiner = new SpecificationJoiner<Role>(criteria.getFilterType());

        if (Objects.nonNull(criteria.getId())) {
            specification = joiner.join(specification, buildSpecification(criteria.getId(), Role_.id));
        }
        if (Objects.nonNull(criteria.getName())) {
            specification = joiner.join(specification, buildStringSpecification(criteria.getName(), Role_.name));
        }
        if (Objects.nonNull(criteria.getDescription())) {
            specification = joiner.join(specification, buildStringSpecification(criteria.getDescription(), Role_.description));
        }

        return specification;
    }

}
