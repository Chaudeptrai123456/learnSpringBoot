package com.example.Aiking.Controller;

import com.example.Aiking.DTO.PostPOJO;
import com.example.Aiking.Entity.Post;
import com.example.Aiking.Service.CloudinaryService;
import com.example.Aiking.Service.Post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/post")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> handleAddNewPost(@RequestBody PostPOJO post) throws Exception {
        try {
            Post result = postService.addNewPost(post);

            return new ResponseEntity<>(result,HttpStatusCode.valueOf(200));
        }catch (Exception e) {
            System.out.print("test controller post "+ e.getMessage() );
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(200));
        }
    }
    @PostMapping("/update")
    public ResponseEntity handleUpdateNewPost(@RequestBody PostPOJO post) throws Exception {
        try {
            Post result = postService.updatePost(post);
            return new ResponseEntity<>(result,HttpStatusCode.valueOf(200));
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(200));
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> handleDeletePost(@RequestBody String title) throws Exception {
        try {
            Optional<?> result = postService.deletePost(title);
            return new ResponseEntity<>(result,HttpStatusCode.valueOf(200));
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(200));
        }
    }
    @PostMapping("/addThumbnail")
    public ResponseEntity<?> handleAddThumbnail(@RequestBody MultipartFile file,String title) {
        Post post = postService.addThumbnail(file,title);
        return new ResponseEntity<>(post,HttpStatusCode.valueOf(200));
    }

}
