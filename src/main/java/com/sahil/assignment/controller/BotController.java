package com.sahil.assignment.controller;

import com.sahil.assignment.dto.ApiResponse;
import com.sahil.assignment.model.Bot;
import com.sahil.assignment.repository.BotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bots")
public class BotController {

    private final BotRepository botRepository;

    public BotController(BotRepository botRepository) {
        this.botRepository = botRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createBot(@RequestBody Bot bot) {
        Bot saved = botRepository.save(bot);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Bot is created successfully", saved));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getBot(@PathVariable Long id) {
        Bot bot = botRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Bot not found"));
        return ResponseEntity.ok(ApiResponse.ok("Bot has been fetched", bot));
    }
}