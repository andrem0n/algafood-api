package com.algaworks.algafoodapi.domain.model;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Restaurante {

  @Id
  private Long id;

  private String nome;

  @Column(name = "taxa_frete")
  private BigDecimal taxaFrete;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Restaurante)) {
      return false;
    }
    Restaurante that = (Restaurante) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public BigDecimal getTaxaFrete() {
    return taxaFrete;
  }

  public void setTaxaFrete(BigDecimal taxaFrete) {
    this.taxaFrete = taxaFrete;
  }
}
