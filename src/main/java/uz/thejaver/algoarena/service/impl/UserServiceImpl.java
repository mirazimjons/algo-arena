package uz.thejaver.algoarena.service.impl;

import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.thejaver.algoarena.domain.User;
import uz.thejaver.algoarena.dto.PasswordChangeDto;
import uz.thejaver.algoarena.mapper.UserMapper;
import uz.thejaver.algoarena.repository.UserRepository;
import uz.thejaver.algoarena.service.UserService;
import uz.thejaver.springbootstarterexceptionsupporter.exception.CommonException;
import uz.thejaver.springbootstarterexceptionsupporter.exception.exceptipTypes.DefaultType;

@Service
public class UserServiceImpl extends UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserMapper mapper,
            UserRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        super(mapper, repository);
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsernameOrThrowException(@NonNull String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CommonException(
                                DefaultType.NOT_FOUND,
                                "User not found with username : %s".formatted(username)
                        )
                );
    }

    @Override
    public Boolean changePassword(@NonNull PasswordChangeDto passwordChangeDto) {
        User user = findByUsernameOrThrowException(passwordChangeDto.getUsername());
        String encodedPassword = passwordEncoder.encode(passwordChangeDto.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return Boolean.TRUE;
    }

}
