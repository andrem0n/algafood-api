package com.algaworks.algafoodapi.domain.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.algaworks.algafoodapi.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.model.Cozinha;
import javax.validation.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CadastroCozinhaIT {

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
}