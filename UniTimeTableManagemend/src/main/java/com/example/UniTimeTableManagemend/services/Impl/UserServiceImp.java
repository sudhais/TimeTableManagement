package com.example.UniTimeTableManagemend.services.Impl;

import com.example.UniTimeTableManagemend.dto.AuthenticationResponse;
import com.example.UniTimeTableManagemend.dto.EnrollmentResponse;
import com.example.UniTimeTableManagemend.dto.RegisterRequest;
import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.TimeTableException;
import com.example.UniTimeTableManagemend.exception.UserException;
import com.example.UniTimeTableManagemend.models.TimeTable;
import com.example.UniTimeTableManagemend.models.User;
import com.example.UniTimeTableManagemend.models.enums.Role;
import com.example.UniTimeTableManagemend.respositories.UserRepository;
import com.example.UniTimeTableManagemend.services.CourseService;
import com.example.UniTimeTableManagemend.services.JwtService;
import com.example.UniTimeTableManagemend.services.TimeTableService;
import com.example.UniTimeTableManagemend.services.UserService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final CourseService courseService;
    private final TimeTableService timeTableService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public List<User> getAllUsers() {
        //get all user in db
        List<User> users = userRepository.findAll();
        //check users are exits or not
        if(users.size() > 0)
            return users;
        else
            return new ArrayList<User>();
    }

    public AuthenticationResponse addNewUser(RegisterRequest request) throws ConstraintViolationException,UserException, CourseException {
        //get the user by given email
        Optional<User> userOptional = userRepository.findUserByEmail(request.getEmail());

        //check user exists or not
        if(userOptional.isPresent()){
            System.out.println("Already user email " + request.getEmail() + " exist" );
            throw new UserException(UserException.AlreadyExists(request.getEmail()));
        }else{

            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.STUDENT)
                    .build();

            //insert data into the db
            userRepository.insert(user);
            System.out.println("inserted " + user);

            var jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .user(user)
                    .build();
        }
    }

    public void updateUser(String userid, User user) throws UserException, ConstraintViolationException, CourseException {

        //get user by given user id or else throw
        User userOptional = userRepository.findById(userid)
                .orElseThrow(()->new UserException(UserException.NotFoundException("User Id",userid)));

        //check user email is same or not
        if(!userOptional.getEmail().equals(user.getEmail())){
            //get the user in db by given email
           User user1 = findUserByEmail(user.getEmail());
           if(user1 != null){
               System.out.println("already email: "+ user1.getEmail() + " exists");
               throw new UserException(UserException.AlreadyExists(user.getEmail()));
           }
           userOptional.setEmail(user.getEmail());
        }
        //check all given course code is exists in db
        for(String courseCode : user.getCourseCodes()){
            courseService.findByCourseCode(courseCode);
        }
        if(user.getRole() != null)
            userOptional.setRole(user.getRole());
        userOptional.setFirstName(user.getFirstName());
        userOptional.setLastName(user.getLastName());
        userOptional.setCourseCodes(user.getCourseCodes());
        userOptional.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(userOptional);
    }

    //get user from db by given name
    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElse(null);
    }

    public TimeTable getTimeTable(String token) throws TimeTableException, CourseException, UserException {
        //extract the email from token
        String email = jwtService.extractUsername(token.substring(7));
        //get the user given by email
        User user = findUserByEmail(email);
        if(user == null){
            throw new UserException(UserException.NotFoundException("User email",email));
        }
       return timeTableService.getTimeTable(user.getCourseCodes());
    }

    public void addCourseEnrollment(String token, String courseCode) throws UserException, CourseException {
        //extract the email from token
        String email = jwtService.extractUsername(token.substring(7));
        //get the user given by email
        User user = findUserByEmail(email);
        //check user null or not
        if(user == null)
            throw new UserException(UserException.NotFoundException("User email" , email));

        if(user.getCourseCodes() != null){
            //check course already not enrolled
            if(!user.getCourseCodes().contains(courseCode)){
                //check course code exists in the db
                courseService.findByCourseCode(courseCode);
                //add course code to the user
                user.getCourseCodes().add(courseCode);
                //update user to the db
                userRepository.save(user);
                return;
            }
        }

        throw new UserException(UserException.AlreadyExistsCode(courseCode));
    }

    public void deleteCourseEnrollment(String token, String courseCode) throws UserException {
        //extract the email from token
        String email = jwtService.extractUsername(token.substring(7));
        //get the user given by email
        User user = findUserByEmail(email);
        //check user null or not
        if(user == null)
            throw new UserException(UserException.NotFoundException("User email" , email));

        if(user.getCourseCodes() != null){
            //check course code enrolled
            if(user.getCourseCodes().contains(courseCode)){
                //add course code to the user
                user.getCourseCodes().remove(courseCode);
                //update user to the db
                userRepository.save(user);
                return;
            }
        }
        throw new UserException(UserException.NotEnrolled(courseCode));
    }

    public EnrollmentResponse getStudentEnrollDetails(String email) throws UserException {
        //get the user given by email
        User user = findUserByEmail(email);
        //check user null or not
        if(user == null)
            throw new UserException(UserException.NotFoundException("User email" , email));
        return EnrollmentResponse.builder()
                .email(user.getEmail())
                .courseCodes(user.getCourseCodes())
                .build();
    }

    public EnrollmentResponse updateStudentEnrollDetails(EnrollmentResponse enroll) throws UserException, CourseException {
        //get the user given by email
        User user = findUserByEmail(enroll.getEmail());
        //check user null or not
        if(user == null)
            throw new UserException(UserException.NotFoundException("User email" , enroll.getEmail()));

        if(user.getRole() == Role.ADMIN || user.getRole() == Role.FACULTY)
            throw new UserException(UserException.RoleConflict(user.getRole()));

        //check all given course code is exists in db
        if(enroll.getCourseCodes() != null){
            for(String courseCode : enroll.getCourseCodes()){
                courseService.findByCourseCode(courseCode);
            }
        }
        user.setCourseCodes(enroll.getCourseCodes());
        userRepository.save(user);
        return enroll;
    }
}
