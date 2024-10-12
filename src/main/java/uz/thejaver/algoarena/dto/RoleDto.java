package uz.thejaver.algoarena.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import uz.thejaver.algoarena.domain.enums.Permission;
import uz.thejaver.springbootstarterjpasupporter.dto.AbsUuidAuditableDto;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDto extends AbsUuidAuditableDto {

    @NotBlank
    String name;

    Set<Permission> permissions = new HashSet<>();

}
