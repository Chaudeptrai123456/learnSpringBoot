package com.example.demo.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.Entity.User;
 public interface UserRepository extends JpaRepository<User,Long> {	
	@Query(value="SELECT * FROM user u WHERE u.email = :email",nativeQuery = true )
	public  User getuserByEmail(String email);
	@Query(value="SELECT * FROM user u WHERE u.username = :username" ,nativeQuery = true)
	User findByUsername(String username);
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);	
} 	
