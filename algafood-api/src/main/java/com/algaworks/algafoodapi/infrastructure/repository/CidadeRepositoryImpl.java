package com.algaworks.algafoodapi.infrastructure.repository;

import com.algaworks.algafoodapi.domain.model.Cidade;
import com.algaworks.algafoodapi.domain.repository.CidadeRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Component
public class CidadeRepositoryImpl implements CidadeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Cidade> todos() {
        return entityManager.createQuery("from Cidade", Cidade.class).getResultList();
    }

    @Override
    @Transactional
    public Cidade salvar(Cidade cidade) {
        return entityManager.merge(cidade);
    }

    @Override
    public Cidade findById(Long id) {
        return entityManager.find(Cidade.class, id);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        Cidade cidade = this.findById(id);

        if (Objects.isNull(cidade)) {
            throw new EmptyResultDataAccessException(1);
        }
        entityManager.remove(cidade);
    }
}
