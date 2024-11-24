package com.koyeb.rayshan.configs;

import com.koyeb.rayshan.utils.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtSecurityFilter implements WebFilter {
    private final JwtUtil jwtUtil;

    public JwtSecurityFilter(JwtUtil util) {
        this.jwtUtil = util;
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            return jwtUtil.getAllClaimsFromTokenMono(token)
                    .flatMap(claims -> {
                        String username = claims.getSubject();
                        List<String> roles = claims.get("roles", List.class);

                        List<SimpleGrantedAuthority> authorities = roles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                .collect(Collectors.toList());

                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(username, null, authorities);

                        return chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                    })
                    .onErrorResume(e -> {
                        // Log the error but continue the chain
                        return chain.filter(exchange);
                    });
        }

        return chain.filter(exchange);
    }
}
