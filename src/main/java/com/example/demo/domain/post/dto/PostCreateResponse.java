package com.example.demo.domain.post.dto;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostCreateResponse {
    private final List<UUID> uuids;
}