package com.e5.ems.util;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.e5.ems.exception.AuthenticationException;
import com.e5.ems.service.AuthenticationService;

/**
 * <p>
 *     It is implementation of adding filter
 * </p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    /**
     * <p>
     *     This method is used to validate the header of each request
     * </p>
     * @param authorizationHeader
     *         this contains the header information for validate
     */
    private void validateHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthenticationException("Header is not valid");
        }
    }

    /**
     * <p>
     *     This method is used for filter the each request by check authentication except login and register api call
     * </p>
     * @param request
     *          is used for get the request details
     * @param response
     *          is used for send the response to client if needed
     * @param filterChain
     *          is used for move to next filter
     * @throws ServletException
     *          if any problem occurs in servlet
     * @throws IOException
     *          if any problem occurs in IO
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(request.getRequestURI().contains("login") || request.getRequestURI().contains("register")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authorizationHeader = request.getHeader("Authorization");
        String username;
        try {
            validateHeader(authorizationHeader);
            String jwtToken = authorizationHeader.substring(7);
            username = JwtUtil.extractUsername(jwtToken);
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }
            UserDetails userDetails = authService.loadUserByUsername(username);
            JwtUtil.validateToken(jwtToken, userDetails);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (AuthenticationException e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
        filterChain.doFilter(request, response);
    }
}
