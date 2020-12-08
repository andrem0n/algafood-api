package com.algaworks.algafoodapi.api.controller;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Cidade;
import com.algaworks.algafoodapi.domain.repository.CidadeRepository;
import com.algaworks.algafoodapi.domain.service.CadastroCidadeService;
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
@RequestMapping(value = "/cidades")
public class CidadeController {

  @Autowired
  private CidadeRepository cidadeRepository;

  @Autowired
  private CadastroCidadeService cadastroCidade;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Cidade> listar() {
    return cidadeRepository.findAll();
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<Cidade> findById(@PathVariable Long id) {
    Optional<Cidade> cidade = cidadeRepository.findById(id);

    if (cidade.isPresent()) {
      return ResponseEntity.ok(cidade.get());
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<Cidade> adicionar(@RequestBody Cidade cidade) {
    return ResponseEntity.status(HttpStatus.CREATED).body(cadastroCidade.salvar(cidade));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Cidade> atualizar(@RequestBody Cidade cidade, @PathVariable Long id) {
    Optional<Cidade> cidadeSalva = cidadeRepository.findById(id);

    if (cidadeSalva.isPresent()) {
      BeanUtils.copyProperties(cidade, cidadeSalva.get(), "id");
      Cidade cidadeAtualizada = cidadeRepository.save(cidadeSalva.get());
      return ResponseEntity.ok(cidadeAtualizada);
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Cidade> remover(@PathVariable Long id) {
    try {
      cadastroCidade.excluir(id);
      return ResponseEntity.noContent().build();
    } catch (EntidadeNaoEncontradaException e) {
      return ResponseEntity.notFound().build();
    } catch (EntidadeEmUsoException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }

}
