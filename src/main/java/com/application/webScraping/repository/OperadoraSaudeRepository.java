package com.application.webScraping.repository;

import com.application.webScraping.model.operadoraSaude.OperadoraSaudeEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperadoraSaudeRepository extends JpaRepository<OperadoraSaudeEntity, Long> {

  @Query("SELECT o FROM OperadoraSaudeEntity o WHERE " +
          "LOWER(o.razaoSocial) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
          "LOWER(o.nomeFantasia) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
          "LOWER(o.cidade) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
          "LOWER(o.uf) = LOWER(:query) OR " +
          "o.cnpj = :query")
  List<OperadoraSaudeEntity> buscaAvancada(@Param("query") String query, Pageable pageable);
}
