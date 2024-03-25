package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.TimeTableException;
import com.example.UniTimeTableManagemend.exception.UserException;
import com.example.UniTimeTableManagemend.models.TimeTable;
import com.example.UniTimeTableManagemend.models.User;
import com.example.UniTimeTableManagemend.respositories.UserRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private CourseService courseService;
    private TimeTableService timeTableService;
    public List<User> getAllUsers() {
        //get all user in db
        List<User> users = userRepository.findAll();
        //check users are exits or not
        if(users.size() > 0)
            return users;
        else
            return new ArrayList<User>();
    }

    public User addNewUser(User user) throws ConstraintViolationException,UserException, CourseException {
        //get the user by given email
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());

        //check user exists or not
        if(userOptional.isPresent()){
            System.out.println("Already user email " + user.getEmail() + " exist" );
            throw new UserException(UserException.AlreadyExists(user.getEmail()));
        }else{
            System.out.println(user.getCourseCodes());
            //check all course code that are correct
            for(String courseCode : user.getCourseCodes()){
                courseService.findByCourseCode(courseCode);
            }
            //insert data into the db
            userRepository.insert(user);
            System.out.println("inserted " + user);
            return user;
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

        userRepository.save(userOptional);
    }

    //get user from db by given name
    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElse(null);
    }

    public TimeTable getTimeTable(String email) throws TimeTableException, CourseException, UserException {
        User user = findUserByEmail(email);
        if(user == null){
            throw new UserException(UserException.NotFoundException("User email",email));
        }
       return timeTableService.getTimeTable(user.getCourseCodes());
    }

    public void addCourseEnrollment(String email, String courseCode) throws UserException, CourseException {
        //get the user given by email
        User user = findUserByEmail(email);
        //check user null or not
        if(user == null)
            throw new UserException(UserException.NotFoundException("User email" , email));

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

        throw new UserException(UserException.AlreadyExistsCode(courseCode));
    }

    public void deleteCourseEnrollment(String email, String courseCode) throws UserException {
        //get the user given by email
        User user = findUserByEmail(email);
        //check user null or not
        if(user == null)
            throw new UserException(UserException.NotFoundException("User email" , email));

        //check course code enrolled
        if(user.getCourseCodes().contains(courseCode)){
            //add course code to the user
            user.getCourseCodes().remove(courseCode);
            //update user to the db
            userRepository.save(user);
            return;
        }
        throw new UserException(UserException.NotEnrolled(courseCode));
    }
}
