package com.example.Aiking.Service.Post;

import com.cloudinary.Cloudinary;
import com.example.Aiking.DTO.PostPOJO;
import com.example.Aiking.Entity.Post;
import com.example.Aiking.Entity.User;
import com.example.Aiking.Repository.PostRepository;
import com.example.Aiking.Repository.UserRepository;
import com.example.Aiking.Service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.Optional;


@Service
public class PostService implements  PostServiceImplement{
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinary;
    @Override
    public Post addNewPost(PostPOJO post)  {
            Post newPost = new Post();
            User user = userRepository.findByUsernameOrEmail(post.getUsername(),post.getUsername()).get();
            newPost.setTitle(post.getTitle());
            newPost.setDescription(post.getDescription());
            newPost.setUser(user);
            newPost.setCreateDate(new Date());
            newPost.setUpDate(new Date());
            user.addPosttoList(newPost);
            userRepository.save(user);
            return postRepository.save(newPost);
    }

    @Override
    public Post updatePost(PostPOJO post) throws Exception {
        try {
            Post updatePost = postRepository.findByTitle(post.getTitle()).get();
            updatePost.setUpDate(new Date());
            updatePost.setTitle(post.getTitle());
            updatePost.setDescription(post.getDescription());
            return postRepository.save(updatePost);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Optional<?> deletePost(String title) {
        Optional<?> result = postRepository.deleteByTitle(title);
        return result;
    }

    @Override
    public Post addThumbnail(MultipartFile gif, String title) {
        String url = cloudinary.uploadFile(gif);
        Post post = postRepository.findByTitle(title).get();
        post.setThumbnail(url);
        postRepository.save(post);
        return post;
    }
}
