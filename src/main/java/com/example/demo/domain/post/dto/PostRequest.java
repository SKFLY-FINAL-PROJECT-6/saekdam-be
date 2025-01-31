package com.example.demo.domain.post.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.example.demo.domain.post.PostImage;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private List<PostImage> images;
}
