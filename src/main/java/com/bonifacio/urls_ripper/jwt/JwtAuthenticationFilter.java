package com.bonifacio.urls_ripper.jwt;

import com.bonifacio.urls_ripper.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>
     * Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    /**
     * This function is a filter that checks for a token in the request, validates
     * it, and sets the
     * authentication in the SecurityContextHolder if the token is valid.
     *
     * @param request     The HttpServletRequest object represents the HTTP request
     *                    made by the client to
     *                    the server. It contains information such as the request
     *                    method, headers, parameters, and body.
     * @param response    The `response` parameter is an instance of the
     *                    `HttpServletResponse` class,
     *                    which represents the response that will be sent back to
     *                    the client. It is used to manipulate the
     *                    response headers and body, set cookies, and send the
     *                    response back to the client.
     * @param filterChain The `filterChain` parameter is an object that represents
     *                    the chain of filters
     *                    that will be applied to the incoming request. It allows
     *                    the filter to pass the request and
     *                    response objects to the next filter in the chain, or to
     *                    the servlet if there are no more
     *                    filters.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String token = getTokenFromRequest(request);
        final String username;

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        username = jwtService.getUsernameFromToken(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }


    /**
     * The function extracts a token from the Authorization header of an HTTP
     * request.
     *
     * @param request The "request" parameter is an instance of the
     *                HttpServletRequest class, which
     *                represents an HTTP request made by a client to a server. It
     *                contains information about the
     *                request, such as the request method, headers, parameters, and
     *                body.
     * @return The method is returning a string value, which is the token extracted
     *         from the request
     *         header.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
