/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package com.ca.mfaas.gateway.security.handler;

import com.ca.mfaas.error.ErrorService;
import com.ca.mfaas.rest.response.ApiMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint {
    private final ErrorService errorService;
    private final ObjectMapper mapper;

    public UnauthorizedHandler(ErrorService errorService, ObjectMapper objectMapper) {
        this.errorService = errorService;
        this.mapper = objectMapper;
    }

    /**
     * Handles authentication errors for endpoints access
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.debug("Unauthorized access to '{}' endpoint", request.getRequestURI());
        ApiMessage message = errorService.createApiMessage("com.ca.mfaas.security.authenticationRequired", request.getRequestURI());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        mapper.writeValue(response.getWriter(), message);
    }
}
