package com.example.demo.domain.user;

import java.util.Map;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import java.util.Base64;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

public interface AuthService {
    String generateKakaoLoginUrl();

    String processKakaoCallback(String code);
}

@Service
@RequiredArgsConstructor
@Slf4j
class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${oauth2.kakao.client-id}")
    private String clientId;

    @Value("${oauth2.kakao.redirect-uri}")
    private String redirectUri;

    @Override
    public String generateKakaoLoginUrl() {
        return String.format(
                "https://kauth.kakao.com/oauth/authorize" +
                        "?client_id=%s" +
                        "&redirect_uri=%s" +
                        "&response_type=code",
                clientId,
                redirectUri);
    }

    @Override
    public String processKakaoCallback(String code) {
        Map<String, String> tokenResponse = getKakaoToken(code);
        String idToken = tokenResponse.get("id_token");
        Map<String, Object> payload = parseJwt(idToken);
        System.out.println(payload);

        String id = "kakao-" + payload.get("sub").toString();
        String nickname = "kakao@" + payload.get("nickname").toString();
        String email = payload.get("email") != null ? payload.get("email").toString() : null;

        if (!userRepository.findById(id).isEmpty()) {
            return jwtTokenProvider.createToken(userRepository.findById(id).get());
        }

        User user = User.of(id, nickname, email);
        userRepository.save(user);

        return jwtTokenProvider.createToken(user);
    }

    private Map<String, Object> parseJwt(String idToken) {
        String[] parts = idToken.split("\\.");
        String payload = new String(Base64.getDecoder().decode(parts[1]));

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(payload, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JWT payload", e);
        }
    }

    private Map<String, String> getKakaoToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, String>>() {
                    });

            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("카카오 토큰 요청에 실패했습니다.");
        }
    }
}