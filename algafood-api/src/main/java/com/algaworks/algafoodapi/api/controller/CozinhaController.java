package com.algaworks.algafoodapi.api.controller;

import com.algaworks.algafoodapi.api.model.CozinhasXmlWrapper;
import com.algaworks.algafoodapi.domain.model.Cozinha;
import com.algaworks.algafoodapi.domain.repository.CozinhaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/cozinhas")
public class CozinhaController {

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Cozinha> listar() {
        return cozinhaRepository.todas();
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public CozinhasXmlWrapper listarXml() {
        return new CozinhasXmlWrapper(cozinhaRepository.todas());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Cozinha> findById(@PathVariable Long id) {
        Cozinha cozinha = cozinhaRepository.porId(id);

        if (Objects.nonNull(cozinha)) {
            return ResponseEntity.ok(cozinhaRepository.porId(id));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Cozinha> adicionar(@RequestBody Cozinha cozinha) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cozinhaRepository.adicionar(cozinha));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cozinha> atualizar(@RequestBody Cozinha cozinha, @PathVariable Long id) {
        Cozinha cozinhaAtual = cozinhaRepository.porId(id);
        if (Objects.nonNull(cozinhaAtual)) {
            BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
            return ResponseEntity.ok(cozinhaRepository.adicionar(cozinhaAtual));
        }
        return ResponseEntity.notFound().build();
    }

}
