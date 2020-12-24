package com.algaworks.algafoodapi.infrastructure.repository.spec;

import com.algaworks.algafoodapi.domain.model.Restaurante;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class RestauranteComNomeSemelhanteSpec implements Specification<Restaurante> {

  private static final long serialVersionUID = 7289573052553436478L;
  private String nome;

  @Override
  public Predicate toPredicate(Root<Restaurante> root, CriteriaQuery<?> query,
      CriteriaBuilder builder) {
    return builder.like(root.get("nome"), "%" + nome + "%");
  }
}
