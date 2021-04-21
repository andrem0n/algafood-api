package com.algaworks.algafoodapi.api.exceptionhandler;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Problema {

  private String mensagem;
  private LocalDateTime dataHora;

}
