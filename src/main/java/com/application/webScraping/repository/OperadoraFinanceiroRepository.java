package com.application.webScraping.repository;

import com.application.webScraping.model.operadoraFinanceira.OperadoraFinanceiroEntity;
import com.application.webScraping.model.operadoraFinanceira.OperadoraFinanceiroEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperadoraFinanceiroRepository extends JpaRepository<OperadoraFinanceiroEntity, OperadoraFinanceiroEntityId> {

}
