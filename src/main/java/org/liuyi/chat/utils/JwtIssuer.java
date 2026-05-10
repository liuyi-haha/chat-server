package org.liuyi.chat.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Data
@Component
public class JwtIssuer {

    @Value("${jwt.private-key-path}")
    private String privateKeyPath;

    private PrivateKey privateKey;

    public static void main(String[] args) throws Exception {
        JwtIssuer issuer = new JwtIssuer();
        issuer.setPrivateKeyPath("config/private.pem");

        issuer.init();
        String token = issuer.generateToken("user123", "file456");
        System.out.println("Generated JWT: " + token);

    }

    @PostConstruct
    public void init() throws Exception {
        this.privateKey = loadPrivateKey(privateKeyPath);
    }

    public String generateToken(String userId, String fileId) {
        return Jwts.builder()
                .claim("user_id", userId)
                .claim("file_id", fileId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 300000))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    private PrivateKey loadPrivateKey(String path) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        content = content.replaceAll("-----BEGIN.*?-----", "")
                .replaceAll("-----END.*?-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(content);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }
}
