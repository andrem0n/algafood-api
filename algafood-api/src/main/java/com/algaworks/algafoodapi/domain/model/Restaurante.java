package com.algaworks.algafoodapi.domain.model;

import com.algaworks.algafoodapi.core.validation.Groups;
import com.algaworks.algafoodapi.core.validation.Multiplo;
import com.algaworks.algafoodapi.core.validation.TaxaFrete;
import com.algaworks.algafoodapi.core.validation.ValorZeroIncluiDescricao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.groups.ConvertGroup;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@ValorZeroIncluiDescricao(valorField = "taxaFrete", descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
public class Restaurante {

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(nullable = false)
  private String nome;

  @NotNull
  @PositiveOrZero
  @Multiplo(numero = 5)
  @Column(name = "taxa_frete", nullable = false)
  private BigDecimal taxaFrete;

  @Valid
  @ConvertGroup(to = Groups.CozinhaId.class)
  @NotNull
  @ManyToOne//(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Cozinha cozinha;

  @JsonIgnore
  @Embedded
  private Endereco endereco;

  @JsonIgnore
  @CreationTimestamp
  @Column(nullable = false, columnDefinition = "datetime")
  private LocalDateTime dataCadastro;

  @JsonIgnore
  @UpdateTimestamp
  @Column(nullable = false, columnDefinition = "datetime")
  private LocalDateTime dataAtualizacao;

  @JsonIgnore
  @ManyToMany
  @JoinTable(name = "restaurante_forma_pagamento",
      joinColumns = @JoinColumn(name = "restaurante_id"),
      inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
  private List<FormaPagamento> formasPagamento = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "restaurante")
  private List<Produto> produtos = new ArrayList<>();

  public Restaurante(String nome, BigDecimal taxaFrete) {
    this.nome = nome;
    this.taxaFrete = taxaFrete;
  }
}
