package com.algaworks.algafoodapi.domain.model;

import com.algaworks.algafoodapi.Groups;
import com.algaworks.algafoodapi.Groups.CadastroRestaurante;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.GeneratedReferenceTypeDelegate;

@JsonRootName("cozinha")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Cozinha {

  @NotNull(groups = Groups.CadastroRestaurante.class)
  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(nullable = false)
  private String nome;

  @JsonIgnore
  @OneToMany(mappedBy = "cozinha")
  private List<Restaurante> restaurantes = new ArrayList<>();

  public Cozinha(String nome) {
    this.nome = nome;
  }
}
