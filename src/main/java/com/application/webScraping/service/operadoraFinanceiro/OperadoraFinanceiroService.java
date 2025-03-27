package com.application.webScraping.service.operadoraFinanceiro;

import com.application.webScraping.dto.OperadorFinanceiroDto;
import com.application.webScraping.model.OperadoraFinanceiroEntity;
import com.application.webScraping.model.OperadoraFinanceiroEntityId;
import com.application.webScraping.repository.OperadoraFinanceiroRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class OperadoraFinanceiroService {


  private final OperadoraFinanceiroRepository operadoraFinanceiroRepository;

  public OperadoraFinanceiroService(OperadoraFinanceiroRepository operadoraFinanceiroRepository) {
    this.operadoraFinanceiroRepository = operadoraFinanceiroRepository;
    }

    public void importCsvData(String csvPath) {
      List<OperadorFinanceiroDto> dados = extractCsvData(csvPath);
      List<OperadoraFinanceiroEntity> entities = dados.stream()
              .map(dto -> new OperadoraFinanceiroEntity(dto))
              .toList();
      operadoraFinanceiroRepository.saveAll(entities);
    }

    public List<OperadorFinanceiroDto> extractCsvData(String csvPath){
      try(Reader reader = new FileReader(csvPath)){
        CsvToBean<OperadorFinanceiroDto> csvToBean = new CsvToBeanBuilder<OperadorFinanceiroDto>(reader)
                .withType(OperadorFinanceiroDto.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        return csvToBean.parse();

      } catch (FileNotFoundException e) {
        throw new RuntimeException("Erro ao ler o arquivo CSV: " + csvPath, e);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }


}
