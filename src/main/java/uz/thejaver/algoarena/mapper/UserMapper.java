package uz.thejaver.algoarena.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import uz.thejaver.algoarena.domain.User;
import uz.thejaver.algoarena.dto.UserDto;
import uz.thejaver.springbootstarterjpasupporter.mapper.AbsMapper;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper extends AbsMapper<UUID, User, UserDto> {
}
