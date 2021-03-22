package com.algaworks.algafoodapi.domain.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@NoArgsConstructor
public class Pedido {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(nullable = false)
  private BigDecimal subtotal;

  @Column(nullable = false)
  private BigDecimal taxaFrete;

  @Column(nullable = false)
  private BigDecimal valorTotal;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime dataCriacao;

  private LocalDateTime dataConfirmacao;
  private LocalDateTime dataCancelamento;
  private LocalDateTime dataEntrega;

  private StatusPedido status;

  @Embedded
  private Endereco enderecoEntrega;

  @ManyToOne
  @JoinColumn(nullable = false)
  private FormaPagamento formaPagamento;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Restaurante restaurante;

  @ManyToOne
  @JoinColumn(name = "usuario_cliente_id", nullable = false)
  private Usuario usuario;

  @OneToMany(mappedBy = "pedido")
  private List<ItemPedido> itens = new ArrayList<>();
}
