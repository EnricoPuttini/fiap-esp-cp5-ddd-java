package br.com.fiap.bank.plataforma.presentation.exception;

import java.time.LocalDateTime;
import java.util.List;

/** Corpo padronizado de erro devolvido pela API. */
public record ErrorResponse(LocalDateTime timestamp, int status, String error, String message,
                            List<String> detalhes) {

    public ErrorResponse(int status, String error, String message) {
        this(LocalDateTime.now(), status, error, message, List.of());
    }

    public ErrorResponse(int status, String error, String message, List<String> detalhes) {
        this(LocalDateTime.now(), status, error, message, detalhes);
    }
}
