package com.algaworks.algafoodapi.api.controller;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Cidade;
import com.algaworks.algafoodapi.domain.repository.CidadeRepository;
import com.algaworks.algafoodapi.domain.service.CadastroCidadeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidade;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Cidade> listar() {
        return cidadeRepository.todos();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Cidade> findById(@PathVariable Long id) {
        Cidade cidade = cidadeRepository.findById(id);

        if (Objects.nonNull(cidade)) {
            return ResponseEntity.ok(cidadeRepository.findById(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Cidade> adicionar(@RequestBody Cidade cidade) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastroCidade.salvar(cidade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cidade> atualizar(@RequestBody Cidade cidade, @PathVariable Long id) {
        Cidade cidadeAtual = cidadeRepository.findById(id);
        if (Objects.nonNull(cidadeAtual)) {
            BeanUtils.copyProperties(cidade, cidadeAtual, "id");
            return ResponseEntity.ok(cidadeRepository.salvar(cidadeAtual));
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
