package com.example.UniTimeTableManagemend.services;

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
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.size() > 0)
            return users;
        else
            return new ArrayList<User>();
    }

    public void addNewUser(User user) throws UserException, ConstraintViolationException {
        Optional<User> userOptional = userRepository.findUserByName(user.getName());

        if(userOptional.isPresent()){
            System.out.println("Already user name " + user.getName() + " exist" );
            throw new UserException(UserException.AlreadyExists(user.getName()));
        }else{
            userRepository.insert(user);
            System.out.println("inserted " + user);
        }
    }
}
