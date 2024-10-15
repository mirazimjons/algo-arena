package uz.thejaver.algoarena.service.impl;

import org.springframework.stereotype.Service;
import uz.thejaver.algoarena.mapper.UserMapper;
import uz.thejaver.algoarena.repository.UserRepository;
import uz.thejaver.algoarena.service.UserService;

@Service
public class UserServiceImpl extends UserService {
    public UserServiceImpl(UserMapper mapper, UserRepository repository) {
        super(mapper, repository);
    }
}
