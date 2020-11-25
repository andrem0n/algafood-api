package com.algaworks.algafoodapi.infrastructure.repository;

import com.algaworks.algafoodapi.domain.model.Estado;
import com.algaworks.algafoodapi.domain.repository.EstadoRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Component
public class EstadoRepositoryImpl implements EstadoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Estado> todos() {
        return entityManager.createQuery("from Estado", Estado.class).getResultList();
    }

    @Override
    @Transactional
    public Estado salvar(Estado estado) {
        return entityManager.merge(estado);
    }

    @Override
    public Estado findById(Long id) {
        return entityManager.find(Estado.class, id);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        Estado estado = this.findById(id);

        if (Objects.isNull(estado)) {
            throw new EmptyResultDataAccessException(1);
        }
        entityManager.remove(estado);
    }
}
