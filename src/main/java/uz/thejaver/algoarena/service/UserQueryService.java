package uz.thejaver.algoarena.service;

import jakarta.persistence.criteria.JoinType;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import uz.thejaver.algoarena.domain.Role_;
import uz.thejaver.algoarena.domain.User;
import uz.thejaver.algoarena.domain.User_;
import uz.thejaver.algoarena.domain.criteria.UserCriteria;
import uz.thejaver.algoarena.dto.UserDto;
import uz.thejaver.algoarena.mapper.UserMapper;
import uz.thejaver.algoarena.repository.UserRepository;
import uz.thejaver.springbootstarterjpasupporter.entity.AbsUuidAuditableEntity_;
import uz.thejaver.springbootstarterjpasupporter.service.AbsQueryService;

import java.util.Objects;
import java.util.UUID;

@Service
public class UserQueryService extends AbsQueryService<UUID, User, UserDto, UserCriteria> {
    public UserQueryService(UserRepository repository, UserMapper mapper) {
        super(repository, mapper);
    }

    @Override
    protected Specification<User> modifySpecification(@NonNull Specification<User> specification, @NonNull UserCriteria criteria) {
        SpecificationJoiner<User> joiner = new SpecificationJoiner<>(criteria.getFilterType());

        if (Objects.nonNull(criteria.getId())) {
            specification = joiner.join(specification, buildSpecification(criteria.getId(), AbsUuidAuditableEntity_.id));
        }
        if (Objects.nonNull(criteria.getUsername())) {
            specification = joiner.join(specification, buildStringSpecification(criteria.getUsername(), User_.username));
        }
        if (Objects.nonNull(criteria.getRoleId())) {
            specification = joiner.join(specification, buildSpecification(criteria.getRoleId(), root -> root.join(User_.roles, JoinType.LEFT).get(Role_.id)));
        }

        return specification;
    }

}
