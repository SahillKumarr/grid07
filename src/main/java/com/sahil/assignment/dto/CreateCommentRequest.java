package com.sahil.assignment.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;


@Data
public class CreateCommentRequest {

    @NotNull
    private Long userId;

    private Long botId;

    private Long humanId;
    @NotBlank
    private String content;
    @NotNull
    private Integer depthLevel;

    @JsonProperty("isBot")
    private boolean isBot;
}