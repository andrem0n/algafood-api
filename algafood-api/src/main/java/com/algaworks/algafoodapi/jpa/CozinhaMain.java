package com.algaworks.algafoodapi.jpa;

import com.algaworks.algafoodapi.AlgafoodApiApplication;
import com.algaworks.algafoodapi.domain.model.Cozinha;
import com.algaworks.algafoodapi.domain.repository.CozinhaRepository;
import java.util.List;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class CozinhaMain {

  public static void main(String[] args) {
    ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
        .web(WebApplicationType.NONE)
        .run(args);

//    CozinhaRepository cozinhas = applicationContext.getBean(CozinhaRepository.class);
//
//    List<Cozinha> lista = cozinhas.todas();
//    System.out.println("printando todas as cozinhas");
//    lista.stream().forEach(System.out::println);
//
//    Cozinha cozinha1 = new Cozinha("japonesa");
//    Cozinha cozinha2 = new Cozinha("brasileira");
//
//    System.out.println("\nprintando cozinhas recentemente salvas");
//    System.out.println(cozinhas.salvar(cozinha1));
//    System.out.println(cozinhas.salvar(cozinha2));
//
//    System.out.println("\nbuscando cozinha pelo id");
//    System.out.println(cozinhas.porId(2l));
//
//    cozinha1.setId(1l);
//    System.out.println("\nFazendo update de uma cozinha");
//    System.out.println(cozinhas.salvar(cozinha1));
//
//    System.out.println("\nremovendo uma cozinha");
    //cozinhas.remover(cozinha1);
  }
}
