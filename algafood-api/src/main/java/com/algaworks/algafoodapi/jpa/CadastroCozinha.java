package com.algaworks.algafoodapi.jpa;

import com.algaworks.algafoodapi.domain.model.Cozinha;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CadastroCozinha {

  @PersistenceContext
  private EntityManager entityManager;

  public List<Cozinha> listar(){
    return entityManager.createQuery("from Cozinha", Cozinha.class).getResultList();
  }

  @Transactional
  public Cozinha salvar(Cozinha cozinha){
    return entityManager.merge(cozinha);
  }

  public Cozinha buscarPorId(Long id){
    return entityManager.find(Cozinha.class, id);
  }

  @Transactional
  public void remover(Cozinha cozinha){
    cozinha = this.buscarPorId(cozinha.getId());
    entityManager.remove(cozinha);
  }

}
