package com.algaworks.algafoodapi.domain.service;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.exception.NegocioException;
import com.algaworks.algafoodapi.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafoodapi.domain.model.Cozinha;
import com.algaworks.algafoodapi.domain.model.Restaurante;
import com.algaworks.algafoodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroRestauranteService {

  public static final String MSG_RESTAURANTE_EM_USO = "Restaurante de código %d não pode ser removido, pois está em uso";

  @Autowired
  private RestauranteRepository restauranteRepository;

  @Autowired
  private CadastroCozinhaService cadastroCozinhaService;

  public Restaurante salvar(Restaurante restaurante) {
    Long cozinhaId = restaurante.getCozinha().getId();

    Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);

    restaurante.setCozinha(cozinha);
    return restauranteRepository.save(restaurante);
  }

  public void excluir(Long id) {
    try {
      restauranteRepository.deleteById(id);
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      throw new EntidadeEmUsoException(
          String.format(MSG_RESTAURANTE_EM_USO, id));
    } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
      throw new RestauranteNaoEncontradoException(id);
    }
  }

  public Restaurante buscarOuFalhar(Long id) {
    return restauranteRepository.findById(id).orElseThrow(() -> new RestauranteNaoEncontradoException(id));
  }

  public Object atualizar(Long id, Restaurante restaurante) {
    Restaurante restauranteSalvo = buscarOuFalhar(id);

    BeanUtils.copyProperties(restaurante, restauranteSalvo, "id", "formasPagamento",
        "endereco", "dataCadastro");
    try {
      return this.salvar(restauranteSalvo);
    } catch (EntidadeNaoEncontradaException entidadeNaoEncontradaException) {
      throw new NegocioException(entidadeNaoEncontradaException.getMessage());
    }
  }
}
