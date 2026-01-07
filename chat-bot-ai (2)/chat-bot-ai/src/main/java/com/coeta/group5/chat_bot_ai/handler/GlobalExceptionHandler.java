package com.coeta.group5.chat_bot_ai.handler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //  Handle authentication failures (401 Unauthorized)
    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException(
            AuthenticationException ex, HttpServletResponse response) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized access\"}");
            response.getWriter().flush();
        }
    }

    //  Handle authentication failures (401 Unauthorized)
    @ExceptionHandler(AuthorizationDeniedException.class)
    public void handleAuthorizationException(
            AuthorizationDeniedException ex, HttpServletResponse response) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized access\"}");
            response.getWriter().flush();
        }
    }

    //  Handle access denied exceptions (403 Forbidden)
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(
            AccessDeniedException ex, HttpServletResponse response) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Access denied\"}");
            response.getWriter().flush();
        }
    }

    //  Handle any type exceptions (500 Internal Server Error)
    @ExceptionHandler(Throwable.class)
    public void Throwable(
            Throwable ex, HttpServletResponse response) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Internal Server Error\"}");
            response.getWriter().flush();
        }
    }

    //  Handle invalid URLs (404 Not Found)
    @ExceptionHandler(NoHandlerFoundException.class)
    public void handleNotFoundException(
            NoHandlerFoundException ex, HttpServletResponse response) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Page not found\"}");
            response.getWriter().flush();
        }
    }

    //  Handle all other internal errors (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public void handleGenericException(
            Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Internal server error\", \"message\": \"" + ex.getMessage() + "\"}");
            response.getWriter().flush();
        }
    }
}