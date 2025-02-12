package uz.thejaver.algoarena.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import uz.thejaver.springbootstarterjpasupporter.dto.AbsUuidAuditableDto;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class UserDto extends AbsUuidAuditableDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,63}$")
    String username;

    String password;

    Set<RoleDto> roles = new HashSet<>();

}
