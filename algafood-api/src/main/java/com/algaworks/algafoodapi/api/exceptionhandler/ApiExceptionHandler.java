package com.algaworks.algafoodapi.api.exceptionhandler;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.exception.NegocioException;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(EntidadeNaoEncontradaException.class)
  public ResponseEntity<?> tratarEntidadeNaoEncontradaException(
      EntidadeNaoEncontradaException exception, WebRequest webRequest) {

    return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(),
        HttpStatus.NOT_FOUND, webRequest);
  }

  @ExceptionHandler(NegocioException.class)
  public ResponseEntity<?> tratarNegocioException(NegocioException exception,
      WebRequest webRequest) {

    return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(),
        HttpStatus.BAD_REQUEST, webRequest);
  }

  @ExceptionHandler(EntidadeEmUsoException.class)
  public ResponseEntity<?> tratarEntidadeEmUsoException(
      EntidadeEmUsoException exception, WebRequest webRequest) {

    return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(),
        HttpStatus.CONFLICT, webRequest);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    if (Objects.isNull(body)) {
      body = Problema.builder()
          .mensagem(status.getReasonPhrase())
          .dataHora(LocalDateTime.now())
          .build();
    } else if (body instanceof String) {
      body = Problema.builder()
          .mensagem((String) body)
          .dataHora(LocalDateTime.now())
          .build();
    }

    return super.handleExceptionInternal(ex, body, headers, status, request);
  }
}
