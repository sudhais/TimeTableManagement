package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.dto.AuthenticationResponse;
import com.example.UniTimeTableManagemend.dto.RegisterRequest;
import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.UserException;
import com.example.UniTimeTableManagemend.models.Course;
import com.example.UniTimeTableManagemend.models.User;
import com.example.UniTimeTableManagemend.models.enums.Faculty;
import com.example.UniTimeTableManagemend.models.enums.Role;
import com.example.UniTimeTableManagemend.respositories.CourseRepository;
import com.example.UniTimeTableManagemend.respositories.TimeTableRepository;
import com.example.UniTimeTableManagemend.respositories.UserRepository;
import com.example.UniTimeTableManagemend.services.Impl.CourseServiceImp;
import com.example.UniTimeTableManagemend.services.Impl.TimeTableServiceImp;
import com.example.UniTimeTableManagemend.services.Impl.UserServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TimeTableRepository timeTableRepository;

    @Mock
    private CourseServiceImp courseService;
    @Mock
    private TimeTableServiceImp timeTableService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImp userService;


    @Test
    void getAllStudent_Success(){
        List<User> expectedUser = new ArrayList<>();
        expectedUser.add(init_user());

        when(userRepository.findAll()).thenReturn(expectedUser);

        List<User> actual = userService.getAllUsers();

        assertNotNull(actual);
        assertEquals(actual.size(),expectedUser.size());
        assertEquals(actual.get(0),expectedUser.get(0));
    }

    @Test
    void addNewUser_Success() throws CourseException, UserException {
        User user = init_user();
        RegisterRequest request = new RegisterRequest(
                "sudhais",
                "mohamed",
                "sudhais@gmail.com",
                "s1234",
                Role.ADMIN
        );
        Course course = new Course(
                "SE2020",
                "AS",
                "sddf",
                3,
                Faculty.IT
        );
        Course course1 = new Course(
                "SE2030",
                "AS",
                "sddf",
                3,
                Faculty.IT
        );
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.empty());
//        when(courseRepository.findCourseByCode("SE2020")).thenReturn(Optional.of(course));
//        when(courseRepository.findCourseByCode("SE2030")).thenReturn(Optional.of(course));
        when(userRepository.insert(user)).thenReturn(user);

        AuthenticationResponse actualUser = userService.addNewUser(request);

        assertNotNull(actualUser);
        assertEquals(user,actualUser.getUser());
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


}