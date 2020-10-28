package com.algaworks.algafoodapi.infrastructure.repository;

import com.algaworks.algafoodapi.domain.model.Cozinha;
import com.algaworks.algafoodapi.domain.repository.CozinhaRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Component
public class CozinhaRepositoryImpl implements CozinhaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Cozinha> todas() {
        return entityManager.createQuery("from Cozinha", Cozinha.class).getResultList();
    }

    @Override
    @Transactional
    public Cozinha salvar(Cozinha cozinha) {
        return entityManager.merge(cozinha);
    }

    @Override
    public Cozinha porId(Long id) {
        return entityManager.find(Cozinha.class, id);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        Cozinha cozinha = this.porId(id);
        if (Objects.isNull(cozinha)) {
            throw new EmptyResultDataAccessException(1);
        }
        entityManager.remove(cozinha);
    }
}
