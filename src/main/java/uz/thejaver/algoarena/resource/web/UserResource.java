package uz.thejaver.algoarena.resource.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uz.thejaver.algoarena.dto.UserDto;
import uz.thejaver.algoarena.service.UserService;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserService userService;
//    private final User roleQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserDto user) {
        return userService.save(user);
    }

//    @PutMapping
//    public RoleDto update(@RequestBody @Valid RoleDto role) {
//        return roleService.update(role);
//    }
//
//    @GetMapping("/{id}")
//    public RoleDto findById(@PathVariable("id") UUID id) {
//        return roleService.getById(id)
//                .orElseThrow(() -> new CommonException(DefaultType.NOT_FOUND));
//    }
//
//    @GetMapping
//    public Page<RoleDto> find(RoleCriteria criteria, Pageable pageable) {
//        return roleQueryService.findByCriteria(criteria, pageable);
//    }
//
//    @GetMapping("/count")
//    public Long count(RoleCriteria criteria) {
//        return roleQueryService.countByCriteria(criteria);
//    }
//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable("id") UUID id) {
//        roleService.deleteById(id);
//    }

}
