package com.algaworks.algafoodapi.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public Restaurante(String nome, BigDecimal taxaFrete) {
    this.nome = nome;
    this.taxaFrete = taxaFrete;
  }

  private String nome;
  @Column(name = "taxa_frete")
  private BigDecimal taxaFrete;


}
