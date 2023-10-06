package com.example.Aiking.Repository;

import com.example.Aiking.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value="SELECT * FROM user u WHERE u.userName=:userName",nativeQuery = true)
    public Optional<User> findUserByUserName(String userName);
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String userName);
    Optional<User> findByUsernameOrEmail(String username, String email);

}
