package uz.thejaver.algoarena.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class PermissionDto {

    String name;

}
