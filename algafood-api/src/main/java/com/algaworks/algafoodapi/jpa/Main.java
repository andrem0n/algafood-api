package com.algaworks.algafoodapi.jpa;

import com.algaworks.algafoodapi.AlgafoodApiApplication;
import com.algaworks.algafoodapi.domain.model.Cozinha;
import java.util.List;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class Main {

  public static void main(String[] args) {
    ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
        .web(WebApplicationType.NONE)
        .run(args);

    CadastroCozinha cadastroCozinha = applicationContext.getBean(CadastroCozinha.class);

    List<Cozinha> lista = cadastroCozinha.listar();
    System.out.println("printando todas as cozinhas");
    lista.stream().forEach(System.out::println);

    Cozinha cozinha1 = new Cozinha("japonesa");
    Cozinha cozinha2 = new Cozinha("brasileira");

    System.out.println("\nprintando cozinhas recentemente salvas");
    System.out.println(cadastroCozinha.salvar(cozinha1));
    System.out.println(cadastroCozinha.salvar(cozinha2));

    System.out.println("\nbuscando cozinha pelo id");
    System.out.println(cadastroCozinha.buscarPorId(2l));

    cozinha1.setId(1l);
    System.out.println("\nFazendo update de uma cozinha");
    System.out.println(cadastroCozinha.salvar(cozinha1));

    System.out.println("\nremovendo uma cozinha");
    cadastroCozinha.remover(cozinha1);
  }
}
