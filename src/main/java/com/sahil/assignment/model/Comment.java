package com.sahil.assignment.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name="comments")
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long postId;

    private Long authorId;

    @Column(columnDefinition = "TEXT")
    private String content;


    private int depthLevel;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
