package com.algaworks.algafoodapi.domain.repository;

import com.algaworks.algafoodapi.domain.model.Restaurante;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

}
