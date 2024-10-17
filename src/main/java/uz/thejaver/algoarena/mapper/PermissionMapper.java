package uz.thejaver.algoarena.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import uz.thejaver.algoarena.domain.enums.Permission;
import uz.thejaver.algoarena.dto.PermissionDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {

    @Mapping(target = "name", expression = "java(permission.name())")
    PermissionDto toDto(Permission permission);

}
