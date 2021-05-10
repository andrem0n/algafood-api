package com.algaworks.algafoodapi.api.exceptionhandler;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(EntidadeNaoEncontradaException.class)
  public ResponseEntity<?> handleEntidadeNaoEncontrada(
      EntidadeNaoEncontradaException exception, WebRequest webRequest) {

    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    ProblemType problemType = ProblemType.ENTIDADE_NAO_ENCONTRADA;
    String detail = exception.getMessage();

    Problem problem = createProblemBuilder(httpStatus, problemType, detail).build();

    return handleExceptionInternal(exception, problem, new HttpHeaders(), httpStatus, webRequest);
  }

  @ExceptionHandler(NegocioException.class)
  public ResponseEntity<?> handleNegocio(NegocioException exception,
      WebRequest webRequest) {

    HttpStatus status = HttpStatus.BAD_REQUEST;
    String detail = exception.getMessage();
    ProblemType problemType = ProblemType.ERRO_NEGOCIO;

    Problem problem = createProblemBuilder(status, problemType, detail).build();

    return handleExceptionInternal(exception, problem, new HttpHeaders(), status, webRequest);
  }

  @ExceptionHandler(EntidadeEmUsoException.class)
  public ResponseEntity<?> handleEntidadeEmUso(
      EntidadeEmUsoException exception, WebRequest webRequest) {

    HttpStatus status = HttpStatus.CONFLICT;
    String detail = exception.getMessage();
    ProblemType problemType = ProblemType.ENTIDADE_EM_USO;

    Problem problem = createProblemBuilder(status, problemType, detail).build();

    return handleExceptionInternal(exception, problem, new HttpHeaders(), status, webRequest);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    Throwable rootCause = ExceptionUtils.getRootCause(ex);

    if (rootCause instanceof InvalidFormatException) {
      return handleInvalidFormat((InvalidFormatException) rootCause, headers, status,
          request);
    } else if (rootCause instanceof PropertyBindingException) {
      return handlePropertyBinding((PropertyBindingException) rootCause, headers, status,
          request);
    }

    ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
    String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";

    Problem problem = createProblemBuilder(status, problemType, detail).build();

    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException rootCause,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;

    String path = joinPath(rootCause.getPath());

    String detail = String.format(
        "A propriedade '%s' não existe. Corrija ou mude essa propriedade e tente novamente.", path);

    Problem problem = createProblemBuilder(status, problemType, detail).build();

    return handleExceptionInternal(rootCause, problem, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException exception, HttpHeaders httpHeaders, HttpStatus httpStatus,
      WebRequest webRequest) {

    if (exception instanceof MethodArgumentTypeMismatchException) {
      return handleMethodArgumentTypeMismatch(
          (MethodArgumentTypeMismatchException) exception, httpHeaders, httpStatus, webRequest);
    }
    return super.handleTypeMismatch(exception, httpHeaders, httpStatus, webRequest);
  }

  private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException exception, HttpHeaders httpHeaders, HttpStatus httpStatus,
      WebRequest webRequest) {

    String detail = String.format(
        "O parâmetro de URL '%s' recebeu o valor '%s', que é um tipo inválido. Corrija e informe um valor compatível com com o tipo '%s'",
        exception.getName(), exception.getValue(), exception.getRequiredType().getSimpleName());

    ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

    Problem problem = createProblemBuilder(httpStatus, problemType, detail).build();

    return handleExceptionInternal(exception, problem, httpHeaders, httpStatus, webRequest);
  }

  private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    String path = joinPath(ex.getPath());

    String detail = String.format(
        "A propriedade '%s' recebeu o valor '%s', que é um tipo inválido. Corrija e informe um valor compátivel com o tipo '%s'. ",
        path, ex.getValue(), ex.getTargetType().getSimpleName());

    ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;

    Problem problem = createProblemBuilder(status, problemType, detail).build();

    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    if (Objects.isNull(body)) {
      body = Problem.builder()
          .title(status.getReasonPhrase())
          .status(status.value())
          .build();
    } else if (body instanceof String) {
      body = Problem.builder()
          .title((String) body)
          .status(status.value())
          .build();
    }

    return super.handleExceptionInternal(ex, body, headers, status, request);
  }

  private Problem.ProblemBuilder createProblemBuilder(HttpStatus httpStatus,
      ProblemType problemType, String detail) {

    return Problem.builder()
        .status(httpStatus.value())
        .type(problemType.getUri())
        .title(problemType.getTitle())
        .detail(detail);
  }

  private String joinPath(List<Reference> path) {
    return path.stream().map(reference -> reference.getFieldName()).collect(
        Collectors.joining("."));
  }
}
