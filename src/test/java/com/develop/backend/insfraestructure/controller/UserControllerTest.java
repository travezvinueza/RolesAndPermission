package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.UserDto;
import com.develop.backend.domain.enums.Gender;
import com.develop.backend.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void updateUser_withoutNewImage() throws IOException {
        UserDto userDtoToUpdate = UserDto.builder().id(1L).username("testuser").email("test@example.com").build();
        UserDto expectedUserDto = UserDto.builder().id(1L).username("updateduser").email("updated@example.com").build();

        when(userService.updateUser(any(UserDto.class), any())).thenReturn(expectedUserDto);

        ResponseEntity<UserDto> responseEntity = userController.updateUser(userDtoToUpdate, null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedUserDto, responseEntity.getBody());
        verify(userService).updateUser(eq(userDtoToUpdate), eq(null));
    }

    @Test
    void updateUser_withNewImage() throws IOException {
        UserDto userDtoToUpdate = UserDto.builder().id(1L).username("testuser").email("test@example.com").build();
        MockMultipartFile newImage = new MockMultipartFile("newImage", "newimage.jpg", "image/jpeg", "new image content".getBytes());
        UserDto expectedUserDto = UserDto.builder().id(1L).username("updateduser").email("updated@example.com").imageProfile("newimage_url.jpg").build();

        when(userService.updateUser(any(UserDto.class), any(MultipartFile.class))).thenReturn(expectedUserDto);

        ResponseEntity<UserDto> responseEntity = userController.updateUser(userDtoToUpdate, newImage);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedUserDto, responseEntity.getBody());
        verify(userService).updateUser(eq(userDtoToUpdate), eq(newImage));
    }

    @Test
    void getAllUsers_whenUsersExist() {
        UserDto user1 = UserDto.builder().id(1L).username("user1").email("user1@example.com").gender(Gender.MASCULINE).build();
        UserDto user2 = UserDto.builder().id(2L).username("user2").email("user2@example.com").gender(Gender.FEMENINE).build();
        List<UserDto> expectedUsers = List.of(user1, user2);

        when(userService.getAllUsers()).thenReturn(expectedUsers);

        ResponseEntity<List<UserDto>> responseEntity = userController.getAllUsers();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedUsers, responseEntity.getBody());
        verify(userService).getAllUsers();
    }

    @Test
    void getAllUsers_whenNoUsersExist() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        ResponseEntity<List<UserDto>> responseEntity = userController.getAllUsers();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.emptyList(), responseEntity.getBody());
        verify(userService).getAllUsers();
    }

    @Test
    void getUserById_whenUserFound() {
        Long userId = 1L;
        UserDto expectedUser = UserDto.builder().id(userId).username("testuser").email("test@example.com").gender(Gender.OTHER).build();

        when(userService.getUserById(anyLong())).thenReturn(expectedUser);

        ResponseEntity<UserDto> responseEntity = userController.getUserById(userId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedUser, responseEntity.getBody());
        verify(userService).getUserById(eq(userId));
    }

    @Test
    void deleteUser() {
        Long userIdToDelete = 1L;
        doNothing().when(userService).deleteUser(anyLong());

        ResponseEntity<Void> responseEntity = userController.deleteUser(userIdToDelete);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(userService).deleteUser(eq(userIdToDelete));
    }
}
