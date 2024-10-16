package uz.thejaver.algoarena.resource.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uz.thejaver.algoarena.domain.criteria.UserCriteria;
import uz.thejaver.algoarena.dto.UserDto;
import uz.thejaver.algoarena.service.UserQueryService;
import uz.thejaver.algoarena.service.UserService;
import uz.thejaver.springbootstarterexceptionsupporter.exception.CommonException;
import uz.thejaver.springbootstarterexceptionsupporter.exception.exceptipTypes.DefaultType;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;
    private final UserQueryService userQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserDto user) {
        return userService.save(user);
    }

    @PutMapping
    public UserDto update(@RequestBody @Valid UserDto role) {
        return userService.update(role);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable("id") UUID id) {
        return userService.getById(id)
                .orElseThrow(() -> new CommonException(DefaultType.NOT_FOUND));
    }

    @GetMapping
    public Page<UserDto> find(UserCriteria criteria, Pageable pageable) {
        return userQueryService.findByCriteria(criteria, pageable);
    }

    @GetMapping("/count")
    public Long count(UserCriteria criteria) {
        return userQueryService.countByCriteria(criteria);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID id) {
        userService.deleteById(id);
    }

}
