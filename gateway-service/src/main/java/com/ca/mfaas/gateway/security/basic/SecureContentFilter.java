/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package com.ca.mfaas.gateway.security.basic;

import com.ca.mfaas.gateway.security.login.LoginRequest;
import com.ca.mfaas.gateway.security.token.TokenAuthentication;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SecureContentFilter extends OncePerRequestFilter {
    private static final String BASIC_AUTHENTICATION_PREFIX = "Basic ";

    private final AuthenticationManager authenticationManager;
    private final AuthenticationFailureHandler failureHandler;

    public SecureContentFilter(AuthenticationManager authenticationManager, AuthenticationFailureHandler failureHandler) {
        this.authenticationManager = authenticationManager;
        this.failureHandler = failureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UsernamePasswordAuthenticationToken authenticationToken = getCredentialFromAuthorizationHeader(request);

        if (authenticationToken != null) {
            try {
                Authentication authentication = authenticationManager.authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } catch (AuthenticationException authenticationException) {
                failureHandler.onAuthenticationFailure(request, response, authenticationException);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private UsernamePasswordAuthenticationToken getCredentialFromAuthorizationHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith(BASIC_AUTHENTICATION_PREFIX)) {
            String base64Credentials = header.replaceFirst(BASIC_AUTHENTICATION_PREFIX, "");

            if (!base64Credentials.isEmpty()) {
                String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
                int i = credentials.indexOf(':');
                if (i > 0) {
                    return new UsernamePasswordAuthenticationToken(credentials.substring(0, i), credentials.substring(i + 1));
                }
            }
        }

        return null;
    }
}
