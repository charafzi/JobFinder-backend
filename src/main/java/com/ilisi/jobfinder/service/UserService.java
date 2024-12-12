package com.ilisi.jobfinder.service;
import com.ilisi.jobfinder.dto.LoginRequest;
import com.ilisi.jobfinder.model.User;
import com.ilisi.jobfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    @Autowired
    public PasswordEncoder passwordEncoder;


    public UserService(UserRepository repo) {
        this.repo = repo;
    }
    public User createUser(User user){
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
