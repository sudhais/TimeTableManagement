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
        //get the user by given name
        Optional<User> userOptional = userRepository.findUserByName(user.getName());

        //check user exists or not
        if(userOptional.isPresent()){
            System.out.println("Already user name " + user.getName() + " exist" );
            throw new UserException(UserException.AlreadyExists(user.getName()));
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

        //check uesr name is same or not
        if(!userOptional.getName().equals(user.getName())){
            //get the user in db by given name
           User user1 = findUserByName(user.getName());
           if(user1 != null){
               System.out.println("already username: "+ user1.getName() + " exists");
               throw new UserException(UserException.AlreadyExists(user.getName()));
           }
           userOptional.setName(user.getName());
        }
        //check all given course code is exists in db
        for(String courseCode : user.getCourseCodes()){
            courseService.findByCourseCode(courseCode);
        }
        userOptional.setCourseCodes(user.getCourseCodes());
        userOptional.setRole(user.getRole());
        userRepository.save(userOptional);
    }

    //get user from db by given name
    public User findUserByName(String name){
        return userRepository.findUserByName(name).orElse(null);
    }

    public TimeTable getTimeTable(String userName) throws TimeTableException, CourseException, UserException {
        User user = findUserByName(userName);
        if(user == null){
            throw new UserException(UserException.NotFoundException("User name",userName));
        }
       return timeTableService.getTimeTable(user.getCourseCodes());
    }

    public void addCourseEnrollment(String userName, String courseCode) throws UserException, CourseException {
        //get the user given by userName
        User user = findUserByName(userName);
        //check user null or not
        if(user == null)
            throw new UserException(UserException.NotFoundException("User name" , userName));

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

    public void deleteCourseEnrollment(String userName, String courseCode) throws UserException {
        //get the user given by userName
        User user = findUserByName(userName);
        //check user null or not
        if(user == null)
            throw new UserException(UserException.NotFoundException("User name" , userName));

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
