package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.model.NoObjectExist;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    UserRepository mockUserRepository;
    private User newUser;
    private UserDto extendUserDto;
    private UserService userService;

    @BeforeEach
    void setUp() {
        newUser = User.builder()
                .id(1L)
                .name("aaa")
                .email("u@mail.com")
                .build();
        userService = new UserServiceImpl(mockUserRepository);
    }


    @Test
    void shouldCreateUserTest() {
        Mockito
                .when(mockUserRepository.save(any(User.class)))
                .thenReturn(newUser);
        extendUserDto = UserMapper.getUserDto(newUser);
        UserDto user = userService.saveUser(newUser);
        assertEquals(user.getId(), extendUserDto.getId());
        assertEquals(user.getName(), extendUserDto.getName());
        assertEquals(user.getEmail(), extendUserDto.getEmail());
    }

    @Test
    void shouldUpdateUserTest() {
        User user = User.builder()
                .name("test1")
                .email("test1@mail.com")
                .build();
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(newUser));
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        Mockito
                .when(mockUserRepository.save(any(User.class)))
                .thenReturn(newUser);

        extendUserDto = userService.updateUser(1L, user);
        assertEquals(user.getName(), extendUserDto.getName());
        assertEquals(user.getEmail(), extendUserDto.getEmail());
    }

    @Test
    void shouldGetUser() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.of(newUser));

        UserDto userDto = userService.getUser(1L);
        assertEquals(newUser.getName(), userDto.getName());
        assertEquals(newUser.getEmail(), userDto.getEmail());
        assertEquals(newUser.getId(), userDto.getId());

    }

    @Test
    void shouldGetUserList() {
        List<User> users = List.of(newUser);
        Mockito
                .when(mockUserRepository.findAll())
                .thenReturn(users);
        List<UserDto> userDtos = userService.getUsers();
        assertEquals(users.get(0).getId(), userDtos.get(0).getId());
        assertEquals(users.get(0).getName(), userDtos.get(0).getName());
        assertEquals(users.get(0).getEmail(), userDtos.get(0).getEmail());
    }

    @Test
    void shouldDeleteUser() {
        userService.deleteUser(1L);
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .deleteById(1L);
    }

    @Test
    void getNotUser() {
        Mockito
                .when(mockUserRepository.findById(anyLong()))
                .thenThrow(new NoObjectExist());

        final NoObjectExist exception = assertThrows(
                NoObjectExist.class,
                () -> userService.getUser(100)
        );
    }


}
