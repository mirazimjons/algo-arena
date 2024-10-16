package uz.thejaver.algoarena.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import uz.thejaver.algoarena.domain.User;
import uz.thejaver.algoarena.dto.UserDto;
import uz.thejaver.springbootstarterjpasupporter.mapper.AbsMapper;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper extends AbsMapper<UUID, User, UserDto> {

    @Override
    @Mapping(target = "password", ignore = true)
    UserDto toDto(User entity);

    @Override
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDto dto);

}
