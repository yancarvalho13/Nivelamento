package com.application.webScraping.service.operadoraSaude;

import com.application.webScraping.model.operadoraSaude.OperadoraSaudeEntity;
import com.application.webScraping.repository.OperadoraSaudeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OperadoraSaudeEntityService {
  private OperadoraSaudeRepository operadoraSaudeRepository;

  public OperadoraSaudeEntityService(OperadoraSaudeRepository operadoraSaudeRepository) {
    this.operadoraSaudeRepository = operadoraSaudeRepository;
  }

  public Page<OperadoraSaudeEntity> advancedSearch(String query, Pageable pageable) {
    return operadoraSaudeRepository.advancedSearch(query, pageable);

  }
}
