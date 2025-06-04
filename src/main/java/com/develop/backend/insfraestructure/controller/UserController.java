package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.UserDto;
import com.develop.backend.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v3/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> updateUser(@RequestPart UserDto userDto,
                                              @RequestPart(required = false) MultipartFile newImage) throws IOException {
        return new ResponseEntity<>(userService.updateUser(userDto, newImage), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestParam(required = false) String username,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Page<UserDto> users = userService.getAllUsers(username, page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}