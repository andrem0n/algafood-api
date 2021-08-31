package com.algaworks.algafoodapi.api.controller;

import com.algaworks.algafoodapi.domain.model.Cidade;
import com.algaworks.algafoodapi.domain.model.Estado;
import com.algaworks.algafoodapi.domain.repository.EstadoRepository;
import com.algaworks.algafoodapi.domain.service.CadastroEstadoService;
import java.util.List;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.ResponseStatus;
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
  public Estado findById(@PathVariable Long id) {
    return cadastroEstadoService.buscarOuFalhar(id);
  }

  @PostMapping
  public ResponseEntity<Estado> adicionar(@RequestBody @Valid Estado estado) {
    return ResponseEntity.status(HttpStatus.CREATED).body(cadastroEstadoService.salvar(estado));
  }

  @PutMapping("/{id}")
  public Estado atualizar(@RequestBody @Valid Estado estado, @PathVariable Long id) {
    Estado estadoSalvo = cadastroEstadoService.buscarOuFalhar(id);

    BeanUtils.copyProperties(estado, estadoSalvo, "id");
    Estado estadoAtualizado = estadoRepository.save(estadoSalvo);
    return estadoAtualizado;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable Long id) {
    cadastroEstadoService.excluir(id);
  }
}
