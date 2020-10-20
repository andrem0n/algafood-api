package com.algaworks.algafoodapi.infrastructure.repository;

import com.algaworks.algafoodapi.domain.model.Cozinha;
import com.algaworks.algafoodapi.domain.model.Estado;
import com.algaworks.algafoodapi.domain.repository.CozinhaRepository;
import com.algaworks.algafoodapi.domain.repository.EstadoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class EstadoRepositoryImpl implements EstadoRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Estado> todos(){
    return entityManager.createQuery("from Estado", Estado.class).getResultList();
  }

  @Override
  @Transactional
  public Estado adicionar(Estado estado){
    return entityManager.merge(estado);
  }

  @Override
  public Estado porId(Long id){
    return entityManager.find(Estado.class, id);
  }

  @Override
  @Transactional
  public void remover(Estado estado){
    estado = this.porId(estado.getId());
    entityManager.remove(estado);
  }
}
