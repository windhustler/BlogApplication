package com.josipk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.josipk.config.CustomUserDetails;
import com.josipk.entity.Comment;
import com.josipk.entity.Post;
import com.josipk.entity.User;
import com.josipk.pojo.CommentPojo;
import com.josipk.service.CommentService;
import com.josipk.service.PostService;
import com.josipk.service.UserService;

import java.util.Date;
import java.util.List;

@RestController
public class BlogController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @GetMapping(value="/posts")
    public List<Post> posts(){
        return postService.getAllPosts();
    }

    @GetMapping(value="/the_post/{id}")
    public Post getPostById(@PathVariable Long id){
        return postService.getPost(id);
    }

    @PostMapping(value="/post")
    public String publishPost(@RequestBody Post post){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(post.getDateCreated() == null)
            post.setDateCreated(new Date());
        post.setCreator(userService.getUser(userDetails.getUsername()));
        postService.savePost(post);
        return "Post was published";
    }

    @GetMapping(value="/posts/{username}")
    public List<Post> postsByUser(@PathVariable String username){
        return postService.findByUser(userService.getUser(username));
    }

    @DeleteMapping(value = "/post/{id}")
    public boolean deletePost(@PathVariable Long id){
        return postService.deletePost(id);
    }

    @DeleteMapping(value = "/comment/{id}")
    public boolean deleteComment(@PathVariable Long id){
        return commentService.deletePost(id);
    }


    @GetMapping(value = "/comments/{postId}")
    public List<Comment> getComments(@PathVariable Long postId){
        return commentService.getComments(postId);
    }

    @PostMapping(value = "/post/postComment")
    public boolean postComment(@RequestBody CommentPojo comment){
        Post post = postService.getPost(comment.getPostId());
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User creator = userService.getUser(userDetails.getUsername());
        if(post == null || creator == null)
            return false;

        commentService.comment(new Comment(comment.getText(),post,creator));
        return true;
    }

}
