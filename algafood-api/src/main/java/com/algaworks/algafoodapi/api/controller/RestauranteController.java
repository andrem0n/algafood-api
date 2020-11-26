package com.algaworks.algafoodapi.api.controller;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Estado;
import com.algaworks.algafoodapi.domain.model.Restaurante;
import com.algaworks.algafoodapi.domain.repository.RestauranteRepository;
import com.algaworks.algafoodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @GetMapping
    public List<Restaurante> listar() {
        return restauranteRepository.todos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> porId(@PathVariable Long id) {
        Restaurante restaurante = restauranteRepository.porId(id);

        if (Objects.nonNull(restaurante)) {
            return ResponseEntity.ok(restaurante);
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
            Restaurante restauranteAtual = restauranteRepository.porId(id);

            if (Objects.nonNull(restauranteAtual)) {
                BeanUtils.copyProperties(restaurante, restauranteAtual, "id");
                restauranteAtual = cadastroRestauranteService.salvar(restaurante);
                return ResponseEntity.ok(restauranteAtual);
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
}
