package com.algaworks.algafoodapi.domain.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@NoArgsConstructor
public class ItemPedido {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private int quantidade;

  @Column(nullable = false)
  private BigDecimal precoUnitario;

  @Column(nullable = false)
  private BigDecimal precoTotal;
  private String observacao;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Pedido pedido;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Produto produto;
}
