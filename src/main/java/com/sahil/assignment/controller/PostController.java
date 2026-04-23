package com.sahil.assignment.controller;

import com.sahil.assignment.dto.ApiResponse;
import com.sahil.assignment.dto.CreateCommentRequest;
import com.sahil.assignment.dto.CreatePostRequest;
import com.sahil.assignment.model.Comment;
import com.sahil.assignment.model.Post;
import com.sahil.assignment.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        Post post = postService.createPost(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Post was created successfully", post));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse> addComment(@PathVariable Long postId, @Valid @RequestBody CreateCommentRequest request) {
        Comment comment = postService.addComment(postId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Comment was added successfully", comment));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<ApiResponse> likePost(@PathVariable Long postId, @RequestParam Long userId,
            @RequestParam(defaultValue = "false") boolean isBot) {
        postService.likePost(postId, userId, isBot);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok("Post liked successfully", null));
    }

    @GetMapping("/{postId}/virality")
    public ResponseEntity<ApiResponse> getViralityScore(@PathVariable Long postId) {
        Long score = postService.getViralityScore(postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.ok("Virality score fetched", score));
    }
}