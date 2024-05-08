package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.dto.AuthenticationResponse;
import com.example.UniTimeTableManagemend.dto.RegisterRequest;
import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.UserException;
import com.example.UniTimeTableManagemend.models.User;
import com.example.UniTimeTableManagemend.models.enums.Role;
import com.example.UniTimeTableManagemend.respositories.UserRepository;
import com.example.UniTimeTableManagemend.services.Impl.CourseServiceImp;
import com.example.UniTimeTableManagemend.services.Impl.JwtServiceImpl;
import com.example.UniTimeTableManagemend.services.Impl.TimeTableServiceImp;
import com.example.UniTimeTableManagemend.services.Impl.UserServiceImp;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseServiceImp courseService;
    @Mock
    private TimeTableServiceImp timeTableService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtServiceImpl jwtService;

    @InjectMocks
    private UserServiceImp userService;

    @Test
    void testGetAllUsers_UsersPresent() {
        // Mock data
        List<User> userList = new ArrayList<>();
        userList.add(User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("John@gmail.com")
                .password("Password")
                .role(Role.STUDENT)
                .build()
        );
        userList.add(User.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("Jane@gmail.com")
                .password("Password")
                .role(Role.STUDENT)
                .build()
        );

        // Mock userRepository behavior
        when(userRepository.findAll()).thenReturn(userList);

        // Call the method
        List<User> result = userService.getAllUsers();

        // Verify userRepository.findAll() is called once
        verify(userRepository, times(1)).findAll();

        // Check if the returned list is equal to the mocked list
        assertEquals(userList, result);
    }

    @Test
    void testGetAllUsers_NoUsers() {
        // Mock an empty list
        List<User> emptyList = new ArrayList<>();

        // Mock userRepository behavior
        when(userRepository.findAll()).thenReturn(emptyList);

        // Call the method
        List<User> result = userService.getAllUsers();

        // Verify userRepository.findAll() is called once
        verify(userRepository, times(1)).findAll();

        // Check if the returned list is empty
        assertEquals(emptyList, result);
    }


    @Test
    void testAddNewUser_Success() throws ConstraintViolationException, UserException, CourseException {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john@example.com", "password",Role.STUDENT);

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse expectedResponse = AuthenticationResponse.builder()
                .token("jwtToken")
//                .user(User.builder()
//                        .firstName("John")
//                        .lastName("Doe")
//                        .email("john@example.com")
//                        .password("encodedPassword")
//                        .role(Role.STUDENT)
//                        .build())
                .build();

        AuthenticationResponse actualResponse = userService.addNewUser(request);

        assertEquals(expectedResponse.getToken(), actualResponse.getToken());
//        assertEquals(expectedResponse.getUser().getEmail(), actualResponse.getUser().getEmail());

        verify(userRepository, times(1)).insert(any(User.class));
    }

    @Test
    void testAddNewUser_UserAlreadyExists() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john@example.com", "password",Role.ADMIN);

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        UserException exception = assertThrows(UserException.class, () -> userService.addNewUser(request));
        assertEquals("User email: john@example.com Already exists", exception.getMessage());

        verify(userRepository, never()).insert(any(User.class));
    }

    private User init_user() {
        List<String> courseCode = new ArrayList<>();
        courseCode.add("SE3020");
        User user = new User(
                "1",
                "sudhais",
                "mohamed",
                "sudhais@gmail.com",
                courseCode,
                "1234",
                Role.ADMIN
        );
        user.setId("1");

        return user;
    }

    @Test
    void testAddNewUser_EncodePasswordFailure() {
        RegisterRequest request = new RegisterRequest("John", "Doe", "john@example.com", "password",Role.STUDENT);

        when(userRepository.findUserByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenThrow(new RuntimeException("Password encoding failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.addNewUser(request));
        assertEquals("Password encoding failed", exception.getMessage());

        verify(userRepository, never()).insert(any(User.class));
    }

//    @Test
//    void testUpdateUser_UserFound() throws CourseException, UserException {
//        // Mock data
//        User existingUser = User.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("John@gmail.com")
//                .password("Password")
//                .role(Role.STUDENT)
//                .build();
//        User updatedUser = User.builder()
//                .firstName("Jane")
//                .lastName("Doe")
//                .email("Jane@gmail.com")
//                .password("Password")
//                .courseCodes(List.of("SE3010"))
//                .role(Role.STUDENT)
//                .build();
//
//        // Mock userRepository behavior
//        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
//        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
//
//        // Call the method
//        userService.updateUser("1", updatedUser);
//
//        // Verify userRepository.findById("1") is called once
//        verify(userRepository, times(1)).findById("1");
//
//        // Verify userRepository.save(updatedUser) is called once
//        verify(userRepository, times(1)).save(updatedUser);
//
//        // Verify password encoding
//        verify(passwordEncoder, times(1)).encode(updatedUser.getPassword());
//    }

    @Test
    void testUpdateUser_UserNotFound() {
        // Mock data
        User updatedUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("John@gmail.com")
                .password("Password")
                .role(Role.STUDENT)
                .build();

        // Mock userRepository behavior
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        // Call the method and assert that it throws UserException
        assertThrows(UserException.class, () -> userService.updateUser("1", updatedUser));

        // Verify userRepository.findById("1") is called once
        verify(userRepository, times(1)).findById("1");

        // Verify userRepository.save(updatedUser) is not called
        verify(userRepository, never()).save(updatedUser);

        // Verify password encoding is not called
        verify(passwordEncoder, never()).encode(updatedUser.getPassword());
    }

    @Test
    void testFindUserByEmail_UserFound() {
        // Mock data
        String email = "test@example.com";
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("John@gmail.com")
                .password("Password")
                .role(Role.STUDENT)
                .build();

        // Mock userRepository behavior
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        // Call the method
        User foundUser = userService.findUserByEmail(email);

        // Verify userRepository.findUserByEmail is called once with the given email
        verify(userRepository).findUserByEmail(email);

        // Verify that the found user matches the expected user
        assertEquals(user, foundUser);
    }

    @Test
    void testFindUserByEmail_UserNotFound() {
        // Mock data
        String email = "nonexistent@example.com";

        // Mock userRepository behavior to return empty Optional
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        // Call the method
        User foundUser = userService.findUserByEmail(email);

        // Verify userRepository.findUserByEmail is called once with the given email
        verify(userRepository).findUserByEmail(email);

        // Verify that no user is found
        assertEquals(null, foundUser);
    }


}