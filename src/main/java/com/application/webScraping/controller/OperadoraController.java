package com.application.webScraping.controller;


import com.application.webScraping.model.operadoraSaude.OperadoraSaudeEntity;
import com.application.webScraping.service.operadoraSaude.OperadoraSaudeEntityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("api/operadoras")
public class OperadoraController {

  private OperadoraSaudeEntityService operadoraSaudeEntityService;

  public OperadoraController(OperadoraSaudeEntityService operadoraSaudeEntityService) {
    this.operadoraSaudeEntityService = operadoraSaudeEntityService;
  }

  @GetMapping("/find")
  public ResponseEntity<Map<String, Object>> findOperadoras(
          @RequestParam("query") String query,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "30") int size
  ){
    Pageable pageable = PageRequest.of(page, size);
    Page<OperadoraSaudeEntity> searchResults = operadoraSaudeEntityService.advancedSearch(query, pageable);

    Map<String, Object> response = new HashMap<>();
    response.put("operadoras", searchResults.getContent());
    response.put("currentPage", searchResults.getNumber());
    response.put("totalItems", searchResults.getTotalElements());
    response.put("totalPages", searchResults.getTotalPages());

    return ResponseEntity.ok(response);
  }

}
