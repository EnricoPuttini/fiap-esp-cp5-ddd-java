package br.com.fiap.bank.plataforma.presentation.exception;

import br.com.fiap.bank.plataforma.application.exception.ContaNaoEncontradaAppException;
import br.com.fiap.bank.plataforma.application.exception.CpfInvalidoAppException;
import br.com.fiap.bank.plataforma.application.exception.DecisaoInvalidaAppException;
import br.com.fiap.bank.plataforma.application.exception.ServicoIndisponivelAppException;
import br.com.fiap.bank.plataforma.application.exception.SolicitacaoNaoEncontradaAppException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Tratamento global de exceções: traduz exceptions de aplicação em status HTTP
 * semânticos (400, 404, 409, 503). Antes, qualquer falha virava 500 sem corpo útil.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacao(MethodArgumentNotValidException ex) {
        List<String> detalhes = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        return resposta(HttpStatus.BAD_REQUEST, "Um ou mais campos do payload são inválidos.", detalhes);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handlePayloadIlegivel(Exception ex) {
        return resposta(HttpStatus.BAD_REQUEST,
                "Corpo ou parâmetro da requisição está malformado ou com tipo incorreto.", List.of());
    }

    @ExceptionHandler({CpfInvalidoAppException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleRequisicaoInvalida(RuntimeException ex) {
        return resposta(HttpStatus.BAD_REQUEST, ex.getMessage(), List.of());
    }

    @ExceptionHandler({SolicitacaoNaoEncontradaAppException.class, ContaNaoEncontradaAppException.class})
    public ResponseEntity<ErrorResponse> handleNaoEncontrado(RuntimeException ex) {
        return resposta(HttpStatus.NOT_FOUND, ex.getMessage(), List.of());
    }

    @ExceptionHandler(DecisaoInvalidaAppException.class)
    public ResponseEntity<ErrorResponse> handleConflito(DecisaoInvalidaAppException ex) {
        return resposta(HttpStatus.CONFLICT, ex.getMessage(), List.of());
    }

    @ExceptionHandler(ServicoIndisponivelAppException.class)
    public ResponseEntity<ErrorResponse> handleIndisponivel(ServicoIndisponivelAppException ex) {
        return resposta(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenerico(Exception ex) {
        // Erros do proprio Spring MVC (rota inexistente, metodo nao suportado, midia invalida...)
        // ja carregam o status correto: preservamos em vez de converter tudo em 500.
        if (ex instanceof org.springframework.web.ErrorResponse springError) {
            int status = springError.getStatusCode().value();
            return ResponseEntity.status(status).body(new ErrorResponse(status,
                    HttpStatus.valueOf(status).getReasonPhrase(), springError.getBody().getDetail()));
        }
        log.error("Erro inesperado", ex);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado ao processar a requisição.", List.of());
    }

    private ResponseEntity<ErrorResponse> resposta(HttpStatus status, String mensagem, List<String> detalhes) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(status.value(), status.getReasonPhrase(), mensagem, detalhes));
    }
}
