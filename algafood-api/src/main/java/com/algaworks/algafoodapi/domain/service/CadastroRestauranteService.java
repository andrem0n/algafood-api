package com.algaworks.algafoodapi.domain.service;

import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Cozinha;
import com.algaworks.algafoodapi.domain.model.Restaurante;
import com.algaworks.algafoodapi.domain.repository.CozinhaRepository;
import com.algaworks.algafoodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CadastroRestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    public Restaurante salvar(Restaurante restaurante) {
        Cozinha cozinha = cozinhaRepository.porId(restaurante.getCozinha().getId());

        if (Objects.nonNull(cozinha)) {
            restaurante.setCozinha(cozinha);
            return restauranteRepository.salvar(restaurante);
        }
        throw new EntidadeNaoEncontradaException(
                String.format("Não existe cadastro de cozinha com o código %d", restaurante.getCozinha().getId()));
    }

}
