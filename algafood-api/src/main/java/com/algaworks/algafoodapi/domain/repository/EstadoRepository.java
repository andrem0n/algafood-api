package com.algaworks.algafoodapi.domain.repository;

import com.algaworks.algafoodapi.domain.model.Estado;

import java.util.List;

public interface EstadoRepository {

    List<Estado> todos();

    Estado findById(Long id);

    Estado salvar(Estado estado);

    void remover(Long id);

}
