package com.example.demo.domain.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import lombok.RequiredArgsConstructor;

public interface JwtTokenProvider {
    String createToken(User user);

    boolean validateToken(String token);
}

@Component
@RequiredArgsConstructor
class JwtTokenProviderImpl implements JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProviderImpl.class);

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    @Override
    public String createToken(User user) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getId())
                .claim("name", user.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtExpiration))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return token;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            decoder.decode(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("인증이 유효하지 않습니다.", e);
            return false;
        }
    }
}