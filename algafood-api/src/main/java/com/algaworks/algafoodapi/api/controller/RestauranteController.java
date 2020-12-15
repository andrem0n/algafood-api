package com.algaworks.algafoodapi.api.controller;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Estado;
import com.algaworks.algafoodapi.domain.model.Restaurante;
import com.algaworks.algafoodapi.domain.repository.RestauranteRepository;
import com.algaworks.algafoodapi.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<Restaurante> porId(@PathVariable Long id) {
    Optional<Restaurante> restaurante = restauranteRepository.findById(id);

    if (restaurante.isPresent()) {
      return ResponseEntity.ok(restaurante.get());
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<?> salvar(@RequestBody Restaurante restaurante) {
    try {
      restaurante = cadastroRestauranteService.salvar(restaurante);
      return ResponseEntity.status(HttpStatus.CREATED).body(restaurante);
    } catch (EntidadeNaoEncontradaException entidadeNaoEncontradaException) {
      return ResponseEntity.badRequest().body(entidadeNaoEncontradaException.getMessage());
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {

    try {
      Optional<Restaurante> restauranteSalvo = restauranteRepository.findById(id);

      if (restauranteSalvo.isPresent()) {
        BeanUtils.copyProperties(restaurante, restauranteSalvo.get(), "id");
        Restaurante restauranteAtualizado = cadastroRestauranteService
            .salvar(restauranteSalvo.get());
        return ResponseEntity.ok(restauranteAtualizado);
      }
      return ResponseEntity.notFound().build();
    } catch (EntidadeNaoEncontradaException entidadeNaoEncontradaException) {
      return ResponseEntity.badRequest().body(entidadeNaoEncontradaException.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Estado> remover(@PathVariable Long id) {
    try {
      cadastroRestauranteService.excluir(id);
      return ResponseEntity.noContent().build();
    } catch (EntidadeNaoEncontradaException e) {
      return ResponseEntity.notFound().build();
    } catch (EntidadeEmUsoException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @PatchMapping("/{id}")
  public ResponseEntity<?> atualizarParcial(@PathVariable Long id,
      @RequestBody Map<String, Object> campos) {

    Optional<Restaurante> restauranteSalvo = restauranteRepository.findById(id);

    if (restauranteSalvo.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    merge(campos, restauranteSalvo.get());

    return atualizar(id, restauranteSalvo.get());
  }

  private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
    ObjectMapper objectMapper = new ObjectMapper();
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
    System.out.println(restauranteDestino);
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
}
