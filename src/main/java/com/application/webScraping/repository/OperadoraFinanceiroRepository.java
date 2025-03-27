package com.application.webScraping.repository;

import com.application.webScraping.model.OperadoraFinanceiroEntity;
import com.application.webScraping.model.OperadoraFinanceiroEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperadoraFinanceiroRepository extends JpaRepository<OperadoraFinanceiroEntity, OperadoraFinanceiroEntityId> {

}
