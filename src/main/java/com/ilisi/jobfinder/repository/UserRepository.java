package com.ilisi.jobfinder.repository;

import com.ilisi.jobfinder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    List<User> findAll();
    Optional<User> getUserByGoogleId(String googleId);

}
