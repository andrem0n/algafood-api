package com.algaworks.algafoodapi.api.controller;

import com.algaworks.algafoodapi.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafoodapi.domain.exception.NegocioException;
import com.algaworks.algafoodapi.domain.model.Cidade;
import com.algaworks.algafoodapi.domain.repository.CidadeRepository;
import com.algaworks.algafoodapi.domain.service.CadastroCidadeService;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "/cidades")
public class CidadeController {
//teste
  @Autowired
  private CidadeRepository cidadeRepository;

  @Autowired
  private CadastroCidadeService cadastroCidade;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Cidade> listar() {
    return cidadeRepository.findAll();
  }

  @GetMapping(value = "/{id}")
  public Cidade findById(@PathVariable Long id) {
    return cadastroCidade.buscarOuFalhar(id);
  }

  @PostMapping
  public Cidade adicionar(@RequestBody Cidade cidade) {
    try {
      return cadastroCidade.salvar(cidade);
    } catch (EstadoNaoEncontradoException e) {
      throw new NegocioException(e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public Cidade atualizar(@RequestBody Cidade cidade, @PathVariable Long id) {
    Cidade cidadeSalva = cadastroCidade.buscarOuFalhar(id);

    BeanUtils.copyProperties(cidade, cidadeSalva, "id");

    try {
      return cadastroCidade.salvar(cidadeSalva);
    } catch (EstadoNaoEncontradoException e) {
      throw new NegocioException(e.getMessage(), e);
    }
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable Long id) {
    cadastroCidade.excluir(id);
  }

}
