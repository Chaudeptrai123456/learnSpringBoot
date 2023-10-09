package com.example.Aiking.Repository;

import com.example.Aiking.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Optional<Post> findByTitle(String title);

    Optional<?> deleteByTitle(String title);

}
