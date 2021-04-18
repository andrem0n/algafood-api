package com.algaworks.algafoodapi.domain.service;

import com.algaworks.algafoodapi.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.model.Cidade;
import com.algaworks.algafoodapi.domain.model.Estado;
import com.algaworks.algafoodapi.domain.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroCidadeService {

  public static final String MSG_CIDADE_NAO_ENCONTRADA = "Não existe um cadastro de cidade com o código %d";
  public static final String MSG_CIDADE_EM_USO = "Cidade de código %d não pode ser removida, pois está em uso";

  @Autowired
  private CidadeRepository cidadeRepository;

  @Autowired
  private CadastroEstadoService cadastroEstadoService;

  public Cidade salvar(Cidade cidade) {
    Long estadoId = cidade.getEstado().getId();

    Estado estado = cadastroEstadoService.buscarOuFalhar(estadoId);

    cidade.setEstado(estado);

    return cidadeRepository.save(cidade);
  }

  public void excluir(Long id) {
    try {
      cidadeRepository.deleteById(id);
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      throw new EntidadeEmUsoException(
          String.format(MSG_CIDADE_NAO_ENCONTRADA, id));
    } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
      throw new CidadeNaoEncontradaException(id);
    }
  }

  public Cidade buscarOuFalhar(Long cidadeId) {
    return cidadeRepository.findById(cidadeId)
        .orElseThrow(() -> new CidadeNaoEncontradaException(cidadeId));
  }
}
