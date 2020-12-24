package com.algaworks.algafoodapi.infrastructure.repository.spec;

import com.algaworks.algafoodapi.domain.model.Restaurante;
import java.math.BigDecimal;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class RestauranteComFreteGratisSpec implements Specification<Restaurante> {

  private static final long serialVersionUID = 7289573052553436478L;

  @Override
  public Predicate toPredicate(Root<Restaurante> root, CriteriaQuery<?> query,
      CriteriaBuilder builder) {
    return builder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
  }
}
