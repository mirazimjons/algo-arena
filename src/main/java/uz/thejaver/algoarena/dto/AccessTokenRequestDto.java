package uz.thejaver.algoarena.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccessTokenRequestDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,63}$")
    String username;

    @NotBlank
    String password;

}
