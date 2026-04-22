package com.sahil.assignment.service;

import com.sahil.assignment.dto.CreateCommentRequest;
import com.sahil.assignment.dto.CreatePostRequest;
import com.sahil.assignment.model.Comment;
import com.sahil.assignment.model.Post;
import com.sahil.assignment.repository.CommentRepository;
import com.sahil.assignment.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final RedisGuardrailService redisGuardrailService;
    private final NotificationService notificationService;

    public PostService(PostRepository postRepository, CommentRepository commentRepository,
                       RedisGuardrailService redisGuardrailService, NotificationService notificationService) {

        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.redisGuardrailService = redisGuardrailService;
        this.notificationService = notificationService;
    }


    @Transactional
    public Post createPost(CreatePostRequest request) {
        Post post = new Post();
        post.setUserId(request.getUserId());
        post.setContent(request.getContent());
        return postRepository.save(post);
    }

    @Transactional
    public Comment addComment(Long postId, CreateCommentRequest request) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));

        if (request.isBot()) {
            redisGuardrailService.checkVerticalCap(request.getDepthLevel());
            redisGuardrailService.checkCooldown(request.getBotId(), request.getHumanId());
            redisGuardrailService.checkHorizontalCap(postId);
        }

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(request.getUserId());
        comment.setContent(request.getContent());
        comment.setDepthLevel(request.getDepthLevel());
        Comment saved = commentRepository.save(comment);

        if (request.isBot()) {
            redisGuardrailService.updateViralityScore(postId, "BOT_REPLY");
            notificationService.handleBotInteraction(
                    request.getHumanId(),
                    "Bot " + request.getBotId() + " replied to your post"
            );
        } else {
            redisGuardrailService.updateViralityScore(postId, "HUMAN_COMMENT");
        }

        return saved;
    }

    @Transactional
    public void likePost(Long postId, Long userId, boolean isBot) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));

        if (isBot) {
            redisGuardrailService.updateViralityScore(postId, "BOT_REPLY");
        } else {
            redisGuardrailService.updateViralityScore(postId, "HUMAN_LIKE");
        }
    }

    public Long getViralityScore(Long postId) {
        return redisGuardrailService.getViralityScore(postId);
    }
}