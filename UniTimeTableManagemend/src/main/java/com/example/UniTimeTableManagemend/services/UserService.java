package com.example.UniTimeTableManagemend.services;

import com.example.UniTimeTableManagemend.exception.CourseException;
import com.example.UniTimeTableManagemend.exception.UserException;
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
    public List<User> getAllUsers() {
        //get all user in db
        List<User> users = userRepository.findAll();
        //check users are exits or not
        if(users.size() > 0)
            return users;
        else
            return new ArrayList<User>();
    }

    public void addNewUser(User user) throws ConstraintViolationException,UserException, CourseException {
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
        }
    }

    public void updateUser(String userid, User user) throws UserException, ConstraintViolationException, CourseException {

        //get user by given user id or else throw
        User userOptional = userRepository.findById(userid)
                .orElseThrow(()->new UserException(UserException.NotFoundException(userid)));

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
        Optional<User> userOptional = userRepository.findUserByName(name);
        return userOptional.orElse(null);
    }
}
