package uz.thejaver.algoarena.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import uz.thejaver.springbootstarterjpasupporter.entity.AbsUuidAuditableEntity;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Table(name = "_user")
@Entity
public class User extends AbsUuidAuditableEntity {

    @Column(nullable = false, unique = true, length = 63)
    String username;

    @Column(nullable = false)
    String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "_rel_user_and_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles = new HashSet<>();

}
