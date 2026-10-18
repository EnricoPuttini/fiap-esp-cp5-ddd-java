package br.com.fiap.bank.core.presentation.exception;

import java.time.LocalDateTime;
import java.util.List;

/** Corpo de erro padronizado devolvido pelo {@link GlobalExceptionHandler}. */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        List<String> detalhes) {

    public ErrorResponse(int status, String error, String message) {
        this(LocalDateTime.now(), status, error, message, List.of());
    }

    public ErrorResponse(int status, String error, String message, List<String> detalhes) {
        this(LocalDateTime.now(), status, error, message, detalhes);
    }
}
