package com.algaworks.algafoodapi.domain.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.algaworks.algafoodapi.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.model.Cozinha;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import javax.validation.ConstraintViolationException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CadastroCozinhaIT {

  @LocalServerPort
  private int port;

  @Autowired
  private CadastroCozinhaService cadastroCozinhaService;

  @Before
  public void setUp() throws Exception {

  }

  @Test
  public void testarCadastroCozinhaComSucesso() {
    //cenário
    Cozinha cozinha = new Cozinha();
    cozinha.setNome("chinesa");

    //ação
    cozinha = cadastroCozinhaService.salvar(cozinha);

    //validação
    assertThat(cozinha).isNotNull();
    assertThat(cozinha.getId()).isNotNull();
  }

  @Test(expected = ConstraintViolationException.class)
  public void deveFalharAoCadastrarCozinhaQuandoSemNome() {
    Cozinha cozinha = new Cozinha();
    cozinha.setNome(null);

    cadastroCozinhaService.salvar(cozinha);
  }

  @Test(expected = EntidadeEmUsoException.class)
  public void deveFalharAoExcluirUmaCozinhaEmUso() {
    cadastroCozinhaService.excluir(1L);
  }

  @Test(expected = CozinhaNaoEncontradaException.class)
  public void deveFalharQuandoExcluirCozinhaInexistente() {
    cadastroCozinhaService.excluir(50L);
  }

  @Test
  public void deveretornarStatus200_QuandoConsultarCozinhas() {

    RestAssured
        .enableLoggingOfRequestAndResponseIfValidationFails();

    RestAssured
        .given()
        .basePath("/cozinhas")
        .port(port)
        .accept(ContentType.JSON)
        .when()
        .get()
        .then()
        .statusCode(200);
  }

  @Test
  public void devereConter4Cozinhas_QuandoConsultarCozinhas() {

    RestAssured
        .enableLoggingOfRequestAndResponseIfValidationFails();

    RestAssured
        .given()
        .basePath("/cozinhas")
        .port(port)
        .accept(ContentType.JSON)
        .when()
        .get()
        .then()
        .body("", Matchers.hasSize(4))
        .body("nome", Matchers.hasItems("Indiana", "Tailandesa"));
  }
}