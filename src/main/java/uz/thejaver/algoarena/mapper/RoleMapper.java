package uz.thejaver.algoarena.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import uz.thejaver.algoarena.domain.Role;
import uz.thejaver.algoarena.dto.RoleDto;
import uz.thejaver.springbootstarterjpasupporter.mapper.AbsMapper;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper extends AbsMapper<UUID, Role, RoleDto> {
}
