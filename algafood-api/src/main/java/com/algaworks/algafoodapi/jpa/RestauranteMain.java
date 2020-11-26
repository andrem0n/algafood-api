package com.algaworks.algafoodapi.jpa;

import com.algaworks.algafoodapi.AlgafoodApiApplication;
import com.algaworks.algafoodapi.domain.model.Restaurante;
import com.algaworks.algafoodapi.domain.repository.RestauranteRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

public class RestauranteMain {

  public static void main(String[] args) {
    ApplicationContext applicationContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
        .web(WebApplicationType.NONE)
        .run(args);

    RestauranteRepository restaurantes = applicationContext.getBean(RestauranteRepository.class);

    List<Restaurante> todosRestaurantes = restaurantes.todos();
    System.out.println("printando todos os restaurantes");
    todosRestaurantes.stream().forEach(System.out::println);

    Restaurante restaurante1 = new Restaurante("Companhia das pizzas", new BigDecimal(6.00));
    Restaurante restaurante2 = new Restaurante("Jeronimo's", new BigDecimal(10.00));

    System.out.println("\nprintando restaurantes recentemente salvos");
    System.out.println(restaurantes.salvar(restaurante1));
    System.out.println(restaurantes.salvar(restaurante2));

    System.out.println("\nbuscando restaurante pelo id");
    System.out.println(restaurantes.porId(2l));

    restaurante1.setId(1l);
    System.out.println("\nFazendo update de um restaurante");
    System.out.println(restaurantes.salvar(restaurante1));

    System.out.println("\nremovendo um restaurante");
    restaurantes.remover(1l);
  }
}
