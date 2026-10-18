package br.com.fiap.bank.core.presentation.exception;

import br.com.fiap.bank.core.application.exception.ClienteNaoEncontradoAppException;
import br.com.fiap.bank.core.application.exception.ContaInativaAppException;
import br.com.fiap.bank.core.application.exception.ContaJaCadastradaAppException;
import br.com.fiap.bank.core.application.exception.ContaNaoEncontradaAppException;
import br.com.fiap.bank.core.application.exception.CpfJaCadastradoAppException;
import br.com.fiap.bank.core.application.exception.OperacaoInvalidaAppException;
import br.com.fiap.bank.core.application.exception.SaldoInsuficienteAppException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Tratamento global de exceções: converte exceptions de negócio da camada
 * de aplicação ({@code application.exception}) e de validação de payload
 * em respostas HTTP padronizadas, conforme a matriz de erros previstos do
 * anexo 10.1 (400, 404, 409). Note que esta classe nunca importa
 * {@code domain.exception} - só enxerga o que a camada application expõe.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        ErrorResponse erro = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), "Requisição Inválida",
                "Um ou mais campos do payload são inválidos.", detalhes);
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorResponse erro = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Requisição Inválida", ex.getMessage());
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(OperacaoInvalidaAppException.class)
    public ResponseEntity<ErrorResponse> handleOperacaoInvalida(OperacaoInvalidaAppException ex) {
        ErrorResponse erro = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Requisição Inválida", ex.getMessage());
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler({ClienteNaoEncontradoAppException.class, ContaNaoEncontradaAppException.class})
    public ResponseEntity<ErrorResponse> handleNaoEncontrado(RuntimeException ex) {
        ErrorResponse erro = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Recurso Não Encontrado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler({CpfJaCadastradoAppException.class, ContaJaCadastradaAppException.class})
    public ResponseEntity<ErrorResponse> handleConflito(RuntimeException ex) {
        ErrorResponse erro = new ErrorResponse(HttpStatus.CONFLICT.value(), "Conflito", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler({SaldoInsuficienteAppException.class, ContaInativaAppException.class})
    public ResponseEntity<ErrorResponse> handleRegraDeNegocioConflitante(RuntimeException ex) {
        ErrorResponse erro = new ErrorResponse(HttpStatus.CONFLICT.value(), "Conflito", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenerico(Exception ex) {
        ErrorResponse erro = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro Interno",
                "Ocorreu um erro inesperado ao processar a requisição.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
