/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.brookfieldpropertiesprogram.core.services.impl;

import com.adobe.granite.crypto.CryptoException;
import com.adobe.granite.oauth.jwt.JwsBuilder;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.PrivateKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author patkara
 */
public class JwsBuilderMockClass implements JwsBuilder {

    String iss;
    String sub;
    String aud;
    long exp;
    String scope;
    Map<String, Object> customClaims = new HashMap<>();

    PrivateKey privateKey;

    final JwtBuilder jwtBuilder = Jwts.builder();

    public JwsBuilderMockClass(PrivateKey privateKey) {
        customClaims = new HashMap<>();
        this.privateKey = privateKey;
    }

    @Override
    public String build() throws CryptoException {
        long expiry = Instant.now().getEpochSecond() + (24 * 60 * 60);
        final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

        String payload = "{\"exp\":" + expiry + ",\"iss\":\"9BFD16EE60BDE2EE0A495C4E@AdobeOrg\",\"sub\":\"0BAE4F81612BFD980A495CE5@techacct.adobe.com\",\"https://ims-na1.adobelogin.com/s/ent_marketing_sdk\":true,\"https://ims-na1.adobelogin.com/s/ent_places_sdk\":true,\"aud\":\"https://ims-na1.adobelogin.com/c/542e941a7ad14f888e71c9de96f54f44\"}";

        jwtBuilder.setClaims(customClaims);
        if (privateKey == null) {
            return "eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE2MzExNDA2MzcsImlzcyI6IjlCRkQxNkVFNjBCREUyRUUwQTQ5NUM0RUBBZG9iZU9yZyIsInN1YiI6IjBCQUU0RjgxNjEyQkZEOTgwQTQ5NUNFNUB0ZWNoYWNjdC5hZG9iZS5jb20iLCJodHRwczovL2ltcy1uYTEuYWRvYmVsb2dpbi5jb20vcy9lbnRfbWFya2V0aW5nX3NkayI6dHJ1ZSwiaHR0cHM6Ly9pbXMtbmExLmFkb2JlbG9naW4uY29tL3MvZW50X3BsYWNlc19zZGsiOnRydWUsImF1ZCI6Imh0dHBzOi8vaW1zLW5hMS5hZG9iZWxvZ2luLmNvbS9jLzU0MmU5NDFhN2FkMTRmODg4ZTcxYzlkZTk2ZjU0ZjQ0In0.SWkE7_PuUP-vtypaDQjb-A-uYWyoy2AGf5h3OjhAwk_JVrio97rjFA8RmQ6rPMUJeQ05WwaM6usMhLEDWH-Kypf4v2Nu43PIMMQh88geZjRvWJEGUxTMi4wwhNU10SA7sFI1pirt2wRHgMUZhDleCmfQWfumLlQLxwJ1DOBxxI9qCHw87oHMn-52kU53r_4m25m7WzBC8mUTkG6-2NAHlf30f7gVNhwHN-P9ZWhPsYBYwGaDh1cNLQp13bqLhG5hePGyljb0sYvyPEh0hxFscjx8r7dPdfdwCMBGs5ThVm8hoyTMgmGLB3oKNxPYaljh59MB4S48c_rbACvHnKwCZA";
        } else {
            LocalDateTime currentTime = LocalDateTime.now();
            //String token = jwtBuilder.signWith(SignatureAlgorithm.RS256, privateKey).compact();
            return Jwts.builder()
                    .setClaims(customClaims)
                    .setIssuer("9BFD16EE60BDE2EE0A495C4E@AdobeOrg")
                    .setSubject("0BAE4F81612BFD980A495CE5@techacct.adobe.com")
                    .setId(UUID.randomUUID().toString())
                    .setAudience("https://ims-na1.adobelogin.com/c/542e941a7ad14f888e71c9de96f54f44")
                    .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                    .setExpiration(Date.from(currentTime
                            .plusMinutes(20)
                            .atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(SignatureAlgorithm.RS256, this.privateKey)
                    .compact();
        }
    }

    @Override
    public JwsBuilder setIssuer(String iss) {
        this.iss = iss;
        jwtBuilder.setIssuer(iss);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public JwsBuilder setSubject(String sub) {
        this.sub = sub;
        jwtBuilder.setSubject(sub);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

    @Override
    public JwsBuilder setAudience(String aud) {
        this.aud = aud;
        jwtBuilder.setAudience(aud);
        return this;
    }

    @Override
    public JwsBuilder setExpiresIn(long exp) {
        LocalDateTime currentTime = LocalDateTime.now();
        this.exp = exp;
        jwtBuilder.setExpiration(Date.from(currentTime
                .plusMinutes(exp)
                .atZone(ZoneId.systemDefault()).toInstant()));
        jwtBuilder.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()));
        return this;
    }

    @Override
    public JwsBuilder setScope(String scope) {
        this.scope = scope;

        return this;
    }

    @Override
    public JwsBuilder setCustomClaimsSetField(String key, Object value) {
        this.customClaims.put(key, value);
        return this;
    }

    @Override
    public JwsBuilder setIssuedAt(long issAt) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return this;
    }

}
