package com.algaworks.algafoodapi.api.controller;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Cidade;
import com.algaworks.algafoodapi.domain.model.Estado;
import com.algaworks.algafoodapi.domain.repository.EstadoRepository;
import com.algaworks.algafoodapi.domain.service.CadastroEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/estados")
public class EstadoController {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    @GetMapping
    public List<Estado> listar() {
        return estadoRepository.todos();
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<Estado> findById(@PathVariable Long id) {
        Estado estado = estadoRepository.findById(id);

        if (Objects.nonNull(estado)) {
            return ResponseEntity.ok(estadoRepository.findById(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Estado> adicionar(@RequestBody Estado estado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastroEstadoService.salvar(estado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estado> atualizar(@RequestBody Cidade cidade, @PathVariable Long id) {
        Estado cidestadoAtualdeAtual = estadoRepository.findById(id);
        if (Objects.nonNull(cidestadoAtualdeAtual)) {
            BeanUtils.copyProperties(cidade, cidestadoAtualdeAtual, "id");
            return ResponseEntity.ok(estadoRepository.salvar(cidestadoAtualdeAtual));
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
