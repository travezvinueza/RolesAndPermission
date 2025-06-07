package com.develop.backend.infrastructure.resolver;

import com.develop.backend.application.dto.UserDto;
import com.develop.backend.domain.service.UserService;
import com.develop.backend.infrastructure.resolver.input.UpdateInput;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.develop.backend.infrastructure.resolver.input.UpdateInput.convertToUserDto;

@Validated
@Controller
@RequiredArgsConstructor
public class UserGraphQLController {

    private final UserService userService;

    @QueryMapping
    public Page<UserDto> users(@Argument String username,
                               @Argument @Min(0) int page,
                               @Argument @Min(1) @Max(100) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(username, pageable);
    }

    @QueryMapping
    public UserDto getUserById(@Argument Long id) {
        return userService.getUserById(id);
    }

    @QueryMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers(null, Pageable.unpaged()).getContent();
    }

    @MutationMapping
    public UserDto updateUser(@Argument("input") UpdateInput input,
                              @Argument("newImage") MultipartFile newImage) throws IOException {
        UserDto dto = convertToUserDto(input);
        return userService.updateUser(dto.getId(), dto, newImage);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        userService.deleteUser(id);
        return true;
    }
}
