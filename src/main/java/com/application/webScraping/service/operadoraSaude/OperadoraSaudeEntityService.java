package com.application.webScraping.service.operadoraSaude;

import com.application.webScraping.model.operadoraSaude.OperadoraSaudeEntity;
import com.application.webScraping.repository.OperadoraSaudeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperadoraSaudeEntityService {
  private OperadoraSaudeRepository operadoraSaudeRepository;

  public OperadoraSaudeEntityService(OperadoraSaudeRepository operadoraSaudeRepository) {
    this.operadoraSaudeRepository = operadoraSaudeRepository;
  }

  public List<OperadoraSaudeEntity> getAllOperadoraSaude() {
    return operadoraSaudeRepository.findAll();
  }

}
