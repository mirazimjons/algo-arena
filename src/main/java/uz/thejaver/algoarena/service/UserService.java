package uz.thejaver.algoarena.service;

import lombok.NonNull;
import uz.thejaver.algoarena.domain.User;
import uz.thejaver.algoarena.dto.PasswordChangeDto;
import uz.thejaver.algoarena.dto.UserDto;
import uz.thejaver.algoarena.mapper.UserMapper;
import uz.thejaver.algoarena.repository.UserRepository;
import uz.thejaver.springbootstarterjpasupporter.service.AbsCrudService;

import java.util.UUID;

public abstract class UserService extends AbsCrudService<UUID, User, UserDto> {
    public UserService(UserMapper mapper, UserRepository repository) {
        super(mapper, repository);
    }

    public abstract User findByUsernameOrThrowException(@NonNull String username);

    public abstract Boolean changePassword(PasswordChangeDto passwordChangeDto);

}
