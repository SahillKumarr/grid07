package com.sahil.assignment.dto;


import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;


@Data
public class CreateCommentRequest {

    @NotNull
    private Long userId;
    @NotNull
    private Long botId;
    @NotNull
    private Long humanId;
    @NotBlank
    private String content;
    @NotNull
    private Integer depthLevel;

    private boolean isBot;
}