package com.algaworks.algafoodapi.infrastructure.repository;

import com.algaworks.algafoodapi.domain.model.Cozinha;
import com.algaworks.algafoodapi.domain.model.Restaurante;
import com.algaworks.algafoodapi.domain.repository.CozinhaRepository;
import com.algaworks.algafoodapi.domain.repository.RestauranteRepository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RestauranteRepositoryImpl implements RestauranteRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Restaurante> todos(){
    return entityManager.createQuery("from Restaurante", Restaurante.class).getResultList();
  }

  @Override
  @Transactional
  public Restaurante salvar(Restaurante restaurante){
    return entityManager.merge(restaurante);
  }

  @Override
  public Restaurante porId(Long id){
    return entityManager.find(Restaurante.class, id);
  }

  @Override
  @Transactional
  public void remover(Restaurante restaurante){
    restaurante = this.porId(restaurante.getId());
    entityManager.remove(restaurante);
  }
}
