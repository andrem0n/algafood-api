package com.algaworks.algafoodapi.infrastructure.repository;

import com.algaworks.algafoodapi.domain.model.Restaurante;
import com.algaworks.algafoodapi.domain.repository.RestauranteRepositoryQueries;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial,
      BigDecimal taxaFreteFinal) {

    var jpql = "from Restaurante where nome like :nome and taxaFrete between :taxaInicial and :taxaFinal";

    return entityManager.createQuery(jpql, Restaurante.class)
        .setParameter("nome", "%" + nome + "%")
        .setParameter("taxaInicial", taxaFreteFinal)
        .setParameter("taxaFinal", taxaFreteFinal)
        .getResultList();
  }
}
