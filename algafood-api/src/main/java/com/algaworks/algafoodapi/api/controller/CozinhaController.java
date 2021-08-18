package com.algaworks.algafoodapi.api.controller;

import com.algaworks.algafoodapi.domain.model.Cozinha;
import com.algaworks.algafoodapi.domain.repository.CozinhaRepository;
import com.algaworks.algafoodapi.domain.service.CadastroCozinhaService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.ResponseStatus;
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
  public Cozinha findById(@PathVariable Long id) {
    return cadastroCozinha.buscarOuFalhar(id);
  }

  @PostMapping
  public ResponseEntity<Cozinha> adicionar(@RequestBody @Valid Cozinha cozinha) {
    return ResponseEntity.status(HttpStatus.CREATED).body(cadastroCozinha.salvar(cozinha));
  }

  @PutMapping("/{id}")
  public Cozinha atualizar(@RequestBody Cozinha cozinha, @PathVariable Long id) {
    Cozinha cozinhaSalva = cadastroCozinha.buscarOuFalhar(id);

    BeanUtils.copyProperties(cozinha, cozinhaSalva, "id");

    return cadastroCozinha.salvar(cozinhaSalva);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable Long id) {
    cadastroCozinha.excluir(id);
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
