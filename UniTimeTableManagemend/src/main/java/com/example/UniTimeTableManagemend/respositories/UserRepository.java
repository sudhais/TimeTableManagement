package com.example.UniTimeTableManagemend.respositories;

import com.example.UniTimeTableManagemend.models.User;
import com.example.UniTimeTableManagemend.models.enums.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {

    Optional<User> findUserByEmail(String email);
    List<User> findUserByRole(Role role);
}
