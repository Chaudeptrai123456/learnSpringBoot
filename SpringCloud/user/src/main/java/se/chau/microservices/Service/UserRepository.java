package se.chau.microservices.Service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.chau.microservices.Entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Integer> {

    Optional<UserEntity> findUserByUsername(String username);
    Optional<UserEntity> findUserByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}



