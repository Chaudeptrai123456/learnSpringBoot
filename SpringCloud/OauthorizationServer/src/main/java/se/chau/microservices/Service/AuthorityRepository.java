package se.chau.microservices.Service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.chau.microservices.Entity.Authority;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority,Integer> {
    //findUserByUsername
    Optional<Authority> findAuthorityByName(String name);
}
