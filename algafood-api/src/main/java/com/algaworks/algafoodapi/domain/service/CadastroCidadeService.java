package com.algaworks.algafoodapi.domain.service;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Cidade;
import com.algaworks.algafoodapi.domain.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroCidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    public Cidade salvar(Cidade cidade) {
        return cidadeRepository.salvar(cidade);
    }

    public void excluir(Long id) {
        try {
            cidadeRepository.remover(id);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new EntidadeEmUsoException(
                    String.format("Cozinha de código %d não pode ser removida, pois está em uso", id));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de cozinha com o código %d", id));
        }
    }
}
