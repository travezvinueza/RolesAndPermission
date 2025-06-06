package com.develop.backend.insfraestructure.resolver;

import com.develop.backend.application.dto.UserDto;
import com.develop.backend.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserGraphQLController {

    private final UserService userService;

    @QueryMapping
    public Page<UserDto> users(@Argument String username,
                               @Argument int page,
                               @Argument int size) {
        return userService.getAllUsers(username, page, size);
    }

    @QueryMapping
    public UserDto getUserById(@Argument Long id) {
        return userService.getUserById(id);
    }

    @QueryMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers(null, 0, Integer.MAX_VALUE).getContent();
    }

    @MutationMapping
    public UserDto updateUser(@Argument("input") UserDto userDto,
                              @Argument("newImage") MultipartFile newImage) throws IOException {
        return userService.updateUser(userDto, newImage);
    }

    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        userService.deleteUser(id);
        return true;
    }
}

