package com.algaworks.algafoodapi.api.exceptionhandler;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  public static final String MSG_ERRO_GENERICA_USUARIO_FINAL =
      "Ocorreu um erro interno inesperado no sistema. "
          + "Tente novamente e se o problema persistir, entre em contato com o administrador do sistema.";

  @ExceptionHandler(EntidadeNaoEncontradaException.class)
  public ResponseEntity<?> handleEntidadeNaoEncontrada(
      EntidadeNaoEncontradaException exception, WebRequest webRequest) {

    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
    String detail = exception.getMessage();

    Problem problem = createProblemBuilder(httpStatus, problemType, detail)
        .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

    return handleExceptionInternal(exception, problem, new HttpHeaders(), httpStatus, webRequest);
  }

  @ExceptionHandler(NegocioException.class)
  public ResponseEntity<?> handleNegocio(NegocioException exception,
      WebRequest webRequest) {

    HttpStatus status = HttpStatus.BAD_REQUEST;
    String detail = exception.getMessage();
    ProblemType problemType = ProblemType.ERRO_NEGOCIO;

    Problem problem = createProblemBuilder(status, problemType, detail)
        .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

    return handleExceptionInternal(exception, problem, new HttpHeaders(), status, webRequest);
  }

  @ExceptionHandler(EntidadeEmUsoException.class)
  public ResponseEntity<?> handleEntidadeEmUso(
      EntidadeEmUsoException exception, WebRequest webRequest) {

    HttpStatus status = HttpStatus.CONFLICT;
    String detail = exception.getMessage();
    ProblemType problemType = ProblemType.ENTIDADE_EM_USO;

    Problem problem = createProblemBuilder(status, problemType, detail)
        .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

    return handleExceptionInternal(exception, problem, new HttpHeaders(), status, webRequest);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleUncaught(Exception exception, WebRequest webRequest) {

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;
    ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;

    exception.printStackTrace();

    Problem problem = createProblemBuilder(status, problemType, detail)
        .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

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

    Problem problem = createProblemBuilder(status, problemType, detail)
        .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException rootCause,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;

    String path = joinPath(rootCause.getPath());

    String detail = String.format(
        "A propriedade '%s' não existe. Corrija ou mude essa propriedade e tente novamente.", path);

    Problem problem = createProblemBuilder(status, problemType, detail)
        .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

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

    Problem problem = createProblemBuilder(httpStatus, problemType, detail)
        .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

    return handleExceptionInternal(exception, problem, httpHeaders, httpStatus, webRequest);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception, HttpHeaders httpHeaders, HttpStatus httpStatus,
      WebRequest webRequest) {

    String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

    ProblemType problemType = ProblemType.DADOS_INVALIDOS;

    Problem problem = createProblemBuilder(httpStatus, problemType, detail)
        .userMessage(
            "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
        .build();

    return handleExceptionInternal(exception, problem, httpHeaders, httpStatus, webRequest);
  }

  private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    String path = joinPath(ex.getPath());

    String detail = String.format(
        "A propriedade '%s' recebeu o valor '%s', que é um tipo inválido. Corrija e informe um valor compátivel com o tipo '%s'. ",
        path, ex.getValue(), ex.getTargetType().getSimpleName());

    ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;

    Problem problem = createProblemBuilder(status, problemType, detail)
        .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
      HttpHeaders headers, HttpStatus httpStatus, WebRequest webRequest) {

    String detail = String
        .format("O recurso %s, que você tentou acessa, é inexistente.", ex.getRequestURL());

    ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;

    Problem problem = createProblemBuilder(httpStatus, problemType, detail)
        .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL).build();

    return handleExceptionInternal(ex, problem, headers, httpStatus, webRequest);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    if (Objects.isNull(body)) {
      body = Problem.builder()
          .title(status.getReasonPhrase())
          .status(status.value())
          .timestamp(LocalDateTime.now())
          .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
          .build();
    } else if (body instanceof String) {
      body = Problem.builder()
          .title((String) body)
          .status(status.value())
          .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
          .timestamp(LocalDateTime.now())
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
        .detail(detail)
        .timestamp(LocalDateTime.now());
  }

  private String joinPath(List<Reference> path) {
    return path.stream().map(reference -> reference.getFieldName()).collect(
        Collectors.joining("."));
  }
}
