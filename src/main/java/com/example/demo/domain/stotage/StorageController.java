package com.example.demo.domain.stotage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @PostMapping("/uploadUrls")
    public ResponseEntity<List<String>> generatePresignedUrls(@RequestBody List<String> uuids) {
        return ResponseEntity.ok(storageService.createObjectUploadUrls(uuids));
    }

    @PostMapping("/accessUrls")
    public ResponseEntity<List<String>> generatePresignedAccessUrls(@RequestBody List<String> uuids) {
        return ResponseEntity.ok(storageService.createObjectAccessUrls(uuids));
    }
}
