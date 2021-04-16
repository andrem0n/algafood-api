package com.algaworks.algafoodapi.domain.repository;

import com.algaworks.algafoodapi.domain.model.FormaPagamento;
import com.algaworks.algafoodapi.domain.model.Restaurante;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>,
    RestauranteRepositoryQueries, JpaSpecificationExecutor<Restaurante> {

  @Query("from Restaurante r join fetch r.cozinha")
  List<Restaurante> findAll();

  List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);

  //@Query("from Restaurante where nome like %:nome% and cozinha.id = :id")
  Restaurante consultarPorNome(String nome, @Param("id") Long cozinhaId);

  Restaurante findByNomeContainingAndCozinhaId(String nome, Long cozinhaId);

  Optional<Restaurante> findFirstByNomeContaining(String nome);

  List<Restaurante> findTop2ByNomeContaining(String nome);

  int countByCozinhaId(Long id);

  List<Restaurante> find(String nome, BigDecimal taxaFreteInicial,
      BigDecimal taxaFreteFinal);
}
