package com.zup.desafio_imposto.repositories;

import com.zup.desafio_imposto.models.TaxType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxTypeRepository extends JpaRepository<TaxType, Long> {
    boolean existsByName(String name);

}