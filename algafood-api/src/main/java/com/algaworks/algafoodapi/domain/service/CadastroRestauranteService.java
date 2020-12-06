package com.algaworks.algafoodapi.domain.service;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Cozinha;
import com.algaworks.algafoodapi.domain.model.Restaurante;
import com.algaworks.algafoodapi.domain.repository.CozinhaRepository;
import com.algaworks.algafoodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CadastroRestauranteService {

  @Autowired
  private RestauranteRepository restauranteRepository;

  @Autowired
  private CozinhaRepository cozinhaRepository;

  public Restaurante salvar(Restaurante restaurante) {
    Cozinha cozinha = cozinhaRepository.findById(restaurante.getCozinha().getId()).orElseThrow(
        () -> new EntidadeNaoEncontradaException(String
            .format("Não existe cadastro de cozinha com o código %d",
                restaurante.getCozinha().getId())));

      restaurante.setCozinha(cozinha);
      return restauranteRepository.salvar(restaurante);
  }

  public void excluir(Long id) {
    try {
      restauranteRepository.remover(id);
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      throw new EntidadeEmUsoException(
          String.format("Cozinha de código %d não pode ser removida, pois está em uso", id));
    } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
      throw new EntidadeNaoEncontradaException(
          String.format("Não existe um cadastro de cozinha com o código %d", id));
    }
  }

}
