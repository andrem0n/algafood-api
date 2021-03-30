package com.algaworks.algafoodapi.domain.service;

import com.algaworks.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafoodapi.domain.model.Estado;
import com.algaworks.algafoodapi.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroEstadoService {

  public static final String MSG_ESTADO_EM_USO = "Estado de código %d não pode ser removido, pois está em uso";
  public static final String MSG_ESTADO_NAO_ENCONTRADO = "Não existe um cadastro de estado com o código %d";
  @Autowired
  private EstadoRepository estadoRepository;

  public Estado salvar(Estado estado) {
    return estadoRepository.save(estado);
  }

  public void excluir(Long id) {
    try {
      estadoRepository.deleteById(id);
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      throw new EntidadeEmUsoException(
          String.format(MSG_ESTADO_NAO_ENCONTRADO, id));
    } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
      throw new EntidadeNaoEncontradaException(
          String.format(MSG_ESTADO_NAO_ENCONTRADO, id));
    }
  }

  public Estado buscarOuFalhar(Long id) {
    return estadoRepository.findById(id).orElseThrow(
        () -> new EntidadeNaoEncontradaException(String.format(MSG_ESTADO_NAO_ENCONTRADO, id)));
  }
}
