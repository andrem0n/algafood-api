package com.algaworks.algafoodapi.api.controller;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Cidade;
import com.algaworks.algafoodapi.domain.model.Estado;
import com.algaworks.algafoodapi.domain.repository.EstadoRepository;
import com.algaworks.algafoodapi.domain.service.CadastroEstadoService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/estados")
public class EstadoController {

  @Autowired
  private EstadoRepository estadoRepository;

  @Autowired
  private CadastroEstadoService cadastroEstadoService;

  @GetMapping
  public List<Estado> listar() {
    return estadoRepository.findAll();
  }


  @GetMapping(value = "/{id}")
  public ResponseEntity<Estado> findById(@PathVariable Long id) {
    Optional<Estado> estado = estadoRepository.findById(id);

    if (estado.isPresent()) {
      return ResponseEntity.ok(estado.get());
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<Estado> adicionar(@RequestBody Estado estado) {
    return ResponseEntity.status(HttpStatus.CREATED).body(cadastroEstadoService.salvar(estado));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Estado> atualizar(@RequestBody Cidade cidade, @PathVariable Long id) {
    Optional<Estado> estadoSalvo = estadoRepository.findById(id);

    if (estadoSalvo.isPresent()) {
      BeanUtils.copyProperties(cidade, estadoSalvo.get(), "id");
      Estado estadoAtualizado = estadoRepository.save(estadoSalvo.get());
      return ResponseEntity.ok(estadoAtualizado);
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Estado> remover(@PathVariable Long id) {
    try {
      cadastroEstadoService.excluir(id);
      return ResponseEntity.noContent().build();
    } catch (EntidadeNaoEncontradaException e) {
      return ResponseEntity.notFound().build();
    } catch (EntidadeEmUsoException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }
}
