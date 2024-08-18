package fast.campus.fcss28.filter;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Slf4j
public class CustomSecurityFilter implements Filter {

    JwkProvider jwkProvider;

    public CustomSecurityFilter() throws MalformedURLException {
        jwkProvider = new JwkProviderBuilder(
                new URL("http://localhost:8080/realms/master/protocol/openid-connect/certs")
        ).build();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String authorizationHeader = ((HttpServletRequest) servletRequest).getHeader("Authorization");
            String token = authorizationHeader.substring(7);

            DecodedJWT decodedJWT = JWT.decode(token);

            Jwk jwk = jwkProvider.get(decodedJWT.getKeyId());

            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("http://localhost:8080/realms/master")
                    .build();
            verifier.verify(decodedJWT);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            decodedJWT.getSubject(),
                            "***",
                            List.of(new SimpleGrantedAuthority("SIMPLE_AUTHORITY"))
                    )
            );
        } catch (JWTVerificationException jwtVerificationException) {
            log.error("Verification Exception", jwtVerificationException);
        } catch (Exception e) {
            log.error("Exception", e);
        }

        filterChain.doFilter(servletRequest, servletResponse);

        SecurityContextHolder.clearContext();
    }
}
