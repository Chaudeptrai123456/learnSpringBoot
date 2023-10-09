package com.example.Aiking.Service.Post;

import com.example.Aiking.DTO.PostPOJO;
import com.example.Aiking.Entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

public interface PostServiceImplement {

    Post addNewPost(PostPOJO post) throws Exception;
    Post updatePost(PostPOJO post) throws Exception;

    Optional<?> deletePost(String title);

    Post addThumbnail(MultipartFile gif, String title);

}
