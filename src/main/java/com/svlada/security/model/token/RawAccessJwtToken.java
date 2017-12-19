package com.svlada.security.model.token;

import com.svlada.common.WebUtil;
import com.svlada.common.utils.ApplicationSupport;
import com.svlada.component.repository.UserRepository;
import com.svlada.entity.User;
import com.svlada.security.exceptions.JwtExpiredTokenException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

public class RawAccessJwtToken implements JwtToken {
    private static Logger logger = LoggerFactory.getLogger(RawAccessJwtToken.class);
            
    private String token;
    
    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    /**
     * Parses and validates JWT Token signature.
     * 
     * @throws BadCredentialsException
     * @throws JwtExpiredTokenException
     * 
     */
    public Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            logger.error("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            logger.info("JWT Token is expired", expiredEx);
            throw new JwtExpiredTokenException(this, "JWT Token expired", expiredEx);
        }
    }

    public User loadUserToMemory() {
        String tokenSigningKey = ApplicationSupport.getValue("demo.security.jwt.tokenSigningKey");
        Jws<Claims> claimsJws = parseClaims(tokenSigningKey);
        Claims body = claimsJws.getBody();
        String username = body.getSubject();
        UserRepository userRepository = ApplicationSupport.getBean(UserRepository.class);
        User user = userRepository.findOneByOpenId(username);
        WebUtil.setCurrentUser(user);
        return user;
    }

    @Override
    public String getToken() {
        return token;
    }
}
