package com.ilisi.jobfinder.service;
import com.ilisi.jobfinder.model.User;
import com.ilisi.jobfinder.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository repo;

    public User saveUser(User user){
        return repo.save(user);
    }
    public Optional<User> getUserByEmail(String email){
        return repo.findByEmail(email);
    }
    public List<User> getAllUsers(){
        return repo.findAll();
    }
    public Optional<User> getUserbyGoogleId(String googleId){
        return repo.getUserByGoogleId(googleId);
    }
}
