package com.algaworks.algafoodapi.api.controller;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Cozinha;
import com.algaworks.algafoodapi.domain.repository.CozinhaRepository;
import com.algaworks.algafoodapi.domain.service.CadastroCozinhaService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cozinhas")
public class CozinhaController {

  @Autowired
  private CozinhaRepository cozinhaRepository;

  @Autowired
  private CadastroCozinhaService cadastroCozinha;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Cozinha> listar() {
    return cozinhaRepository.findAll();
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<Cozinha> findById(@PathVariable Long id) {
    Optional<Cozinha> cozinha = cozinhaRepository.findById(id);

    if (cozinha.isPresent()) {
      return ResponseEntity.ok(cozinha.get());
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<Cozinha> adicionar(@RequestBody Cozinha cozinha) {
    return ResponseEntity.status(HttpStatus.CREATED).body(cadastroCozinha.salvar(cozinha));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Cozinha> atualizar(@RequestBody Cozinha cozinha, @PathVariable Long id) {
    Optional<Cozinha> cozinhaSalva = cozinhaRepository.findById(id);

    if (cozinhaSalva.isPresent()) {
      BeanUtils.copyProperties(cozinha, cozinhaSalva.get(), "id");
      return ResponseEntity.ok(cozinhaRepository.save(cozinhaSalva.get()));
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Cozinha> remover(@PathVariable Long id) {
    try {
      cadastroCozinha.excluir(id);
      return ResponseEntity.noContent().build();
    } catch (EntidadeNaoEncontradaException e) {
      return ResponseEntity.notFound().build();
    } catch (EntidadeEmUsoException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

  @GetMapping("/por-nome")
  public List<Cozinha> cozinhasPorNome(String nome) {
    return cozinhaRepository.findTodasByNomeContaining(nome);
  }

  @GetMapping("/unica-por-nome")
  public Optional<Cozinha> cozinhaUnicaPorNome(String nome) {
    return cozinhaRepository.findByNome(nome);
  }

  @GetMapping("/existe-por-nome")
  public boolean existsPorNome(String nome) {
    return cozinhaRepository.existsByNome(nome);
  }


}
