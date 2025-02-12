package com.example.demo.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.demo.domain.post.PostImage;
import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PostCreateRequest {
    private String title;
    private String content;
    private List<PostImage> images;
}
