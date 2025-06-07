package com.develop.backend.infrastructure.controller;

import com.develop.backend.application.dto.UserDto;
import com.develop.backend.domain.enums.Gender;
import com.develop.backend.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void updateUser() throws IOException {
        UserDto userDtoToUpdate = UserDto.builder().id(1L).username("testuser").email("test@example.com").build();
        UserDto expectedUserDto = UserDto.builder().id(1L).username("updateduser").email("updated@example.com").build();

        when(userService.updateUser(eq(1L), eq(userDtoToUpdate), eq(null))).thenReturn(expectedUserDto);

        ResponseEntity<UserDto> responseEntity = userController.updateUser(userDtoToUpdate, null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedUserDto, responseEntity.getBody());

        verify(userService).updateUser(eq(1L), eq(userDtoToUpdate), eq(null));
    }

    @Test
    void getAllUsers() {
        UserDto user1 = UserDto.builder().id(1L).username("user1").email("user1@example.com").gender(Gender.MASCULINE).build();
        UserDto user2 = UserDto.builder().id(2L).username("user2").email("user2@example.com").gender(Gender.FEMININE).build();

        List<UserDto> userList = List.of(user1, user2);
        Page<UserDto> userPage = new PageImpl<>(userList);

        when(userService.getAllUsers(eq(null), any(Pageable.class))).thenReturn(userPage);

        ResponseEntity<Page<UserDto>> response = userController.getAllUsers(null, 0, 10);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals("user1", response.getBody().getContent().get(0).getUsername());
        assertEquals("user2", response.getBody().getContent().get(1).getUsername());

        verify(userService).getAllUsers(eq(null), any(Pageable.class));
    }

    @Test
    void getUserById() {
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