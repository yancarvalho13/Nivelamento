package com.application.webScraping.repository;

import com.application.webScraping.model.operadoraSaude.OperadoraSaudeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperadoraSaudeRepository extends JpaRepository<OperadoraSaudeEntity, Long> {

  @Query("SELECT o FROM OperadoraSaudeEntity o WHERE " +
          "LOWER(o.razaoSocial) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
          "LOWER(o.regAns) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
          "LOWER(o.cidade) = LOWER(:query) OR " +
          "o.cnpj = :query")
  Page<OperadoraSaudeEntity> advancedSearch(@Param("query") String query, Pageable pageable);

}