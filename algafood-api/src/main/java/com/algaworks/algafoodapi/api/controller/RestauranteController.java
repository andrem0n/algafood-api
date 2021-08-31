package com.algaworks.algafoodapi.api.controller;

import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Restaurante;
import com.algaworks.algafoodapi.domain.repository.RestauranteRepository;
import com.algaworks.algafoodapi.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

  @Autowired
  private RestauranteRepository restauranteRepository;

  @Autowired
  private CadastroRestauranteService cadastroRestauranteService;

  @GetMapping
  public List<Restaurante> listar() {
    return restauranteRepository.findAll();
  }

  @GetMapping("/{id}")
  public Restaurante porId(@PathVariable Long id) {
    return cadastroRestauranteService.buscarOuFalhar(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> salvar(
      @RequestBody @Valid Restaurante restaurante) {
    try {
      restaurante = cadastroRestauranteService.salvar(restaurante);
      return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
    } catch (EntidadeNaoEncontradaException entidadeNaoEncontradaException) {
      return ResponseEntity.badRequest().body(entidadeNaoEncontradaException.getMessage());
    }
  }

  @PutMapping("/{id}")
  public Object atualizar(@PathVariable Long id, @RequestBody @Valid Restaurante restaurante) {
    return cadastroRestauranteService.atualizar(id, restaurante);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable Long id) {
    cadastroRestauranteService.excluir(id);
  }

  @PatchMapping("/{id}")
  public Object atualizarParcial(@PathVariable Long id,
      @RequestBody Map<String, Object> campos, HttpServletRequest httpServletRequest) {

    Restaurante restauranteSalvo = cadastroRestauranteService.buscarOuFalhar(id);

    merge(campos, restauranteSalvo, httpServletRequest);

    return atualizar(id, restauranteSalvo);
  }

  private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino,
      HttpServletRequest httpServletRequest) {
    ServletServerHttpRequest httpRequest = new ServletServerHttpRequest(httpServletRequest);

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

      Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

      System.out.println(restauranteOrigem);

      dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
        Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
        field.setAccessible(true);

        Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

        System.out.println(
            nomePropriedade + " = " + novoValor);

        ReflectionUtils.setField(field, restauranteDestino, novoValor);
      });
    } catch (IllegalArgumentException illegalArgumentException) {
      Throwable rootCause = ExceptionUtils.getRootCause(illegalArgumentException);
      throw new HttpMessageNotReadableException(illegalArgumentException.getMessage(), rootCause,
          httpRequest);
    }
  }

  @GetMapping("/por-taxa-frete")
  public List<Restaurante> restaurantesPorTaxaFreteInicialFinal(BigDecimal taxaInicial,
      BigDecimal taxaFinal) {
    return restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);
  }

  @GetMapping("/por-nome-and-cozinhaId")
  public Restaurante restaurantesPorNomeAndCozinhaId(String nome,
      Long cozinhaId) {
    return restauranteRepository.consultarPorNome(nome, cozinhaId);
  }

  @GetMapping("/por-nome")
  public Optional<Restaurante> restaurantePorNome(String nome) {
    return restauranteRepository.findFirstByNomeContaining(nome);
  }

  @GetMapping("/top2-por-nome")
  public List<Restaurante> restaurantesTop2PorNome(String nome) {
    return restauranteRepository.findTop2ByNomeContaining(nome);
  }

  @GetMapping("/count-por-cozinhas")
  public int countPorCozinha(Long id) {
    return restauranteRepository.countByCozinhaId(id);
  }

  @GetMapping("/porNome-and-taxa-frete")
  public List<Restaurante> restaurantesPorNomeFrete(String nome, BigDecimal taxaInicial,
      BigDecimal taxaFinal) {
    return restauranteRepository.find(nome, taxaInicial, taxaFinal);
  }

  @GetMapping("/com-frete-gratis")
  public List<Restaurante> restaurantesComFreteGratis(String nome) {
    return restauranteRepository.findComFreteGratis(nome);
  }

  @GetMapping("/primeiro")
  public Optional<Restaurante> buscarPrimeiro() {
    return restauranteRepository.buscarPrimeiro();
  }
}
