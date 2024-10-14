package uz.thejaver.algoarena.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import uz.thejaver.algoarena.domain.enums.Permission;
import uz.thejaver.springbootstarterjpasupporter.entity.AbsUuidAuditableEntity;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Table(name = "_role")
@Entity
public class Role extends AbsUuidAuditableEntity {

    @Column(nullable = false, unique = true, length = 127)
    String name;

    String description;

    @ElementCollection(targetClass = Permission.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "_rel_role_permission",
            joinColumns = @JoinColumn(name = "role_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    Set<Permission> permissions = new HashSet<>();

}
