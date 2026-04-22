package com.sahil.assignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePostRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String content;
}
