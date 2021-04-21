package com.algaworks.algafoodapi.api.exceptionhandler;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.exception.NegocioException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(EntidadeNaoEncontradaException.class)
  public ResponseEntity<?> tratarEntidadeNaoEncontradaException(
      EntidadeNaoEncontradaException exception) {
    Problema problema = Problema.builder()
        .mensagem(exception.getMessage())
        .dataHora(LocalDateTime.now())
        .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problema);
  }

  @ExceptionHandler(NegocioException.class)
  public ResponseEntity<?> tratarNegocioException(NegocioException exception) {
    Problema problema = Problema.builder()
        .mensagem(exception.getMessage())
        .dataHora(LocalDateTime.now()).build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problema);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<?> tratarHttpMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException exception) {
    Problema problema = Problema.builder()
        .mensagem("O tipo de mídia não é aceito.")
        .dataHora(LocalDateTime.now()).build();

    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(problema);
  }

  @ExceptionHandler(EntidadeEmUsoException.class)
  public ResponseEntity<?> tratarEntidadeEmUsoException(
      EntidadeEmUsoException exception) {
    Problema problema = Problema.builder()
        .mensagem(exception.getMessage())
        .dataHora(LocalDateTime.now()).build();

    return ResponseEntity.status(HttpStatus.CONFLICT).body(problema);
  }


}
