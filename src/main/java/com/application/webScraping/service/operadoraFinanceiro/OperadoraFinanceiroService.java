package com.application.webScraping.service.operadoraFinanceiro;

import com.application.webScraping.repository.OperadoraFinanceiroRepository;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.stereotype.Service;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;


@Service
public class OperadoraFinanceiroService {


  private final OperadoraFinanceiroRepository operadoraFinanceiroRepository;

  public OperadoraFinanceiroService(OperadoraFinanceiroRepository operadoraFinanceiroRepository) {
    this.operadoraFinanceiroRepository = operadoraFinanceiroRepository;
    }

//  public void importCsvData(String csvPath) {
//    List<OperadorFinanceiroDto> dados = extractCsvData(csvPath);
//    List<OperadoraFinanceiroEntity> entities = new ArrayList<>();
//    for (OperadorFinanceiroDto dto : dados) {
//      // Conversão do DTO para entidade (assumindo que o construtor faz o mapeamento)
//      entities.add(new OperadoraFinanceiroEntity(dto));
//    }
//    // Aqui você persiste no banco (por exemplo, utilizando um repository JPA)
//    operadoraFinanceiroRepository.saveAll(entities);
//  }

  public void extractCsvData(String csvPath) {
    int expectedColumns = 6;

    try {
      // Arquivo temporário limpo
      Path tempCsv = Files.createTempFile("cleaned-", ".csv");

      try (BufferedReader reader = new BufferedReader(new FileReader(csvPath));
           BufferedWriter writer = Files.newBufferedWriter(tempCsv)) {

        String line;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
          lineNumber++;

          // Mantém o cabeçalho como está
          if (lineNumber == 1) {
            writer.write(line);
            writer.newLine();
            continue;
          }

          // Verifica se as aspas estão balanceadas
          long quoteCount = line.chars().filter(c -> c == '"').count();
          if (quoteCount % 2 != 0) {
            System.out.println("Linha ignorada por aspas quebradas: " + lineNumber);
            continue;
          }

          // Divide mantendo campos vazios
          String[] columns = line.split(";", -1);

          if (columns.length != expectedColumns) {
            System.out.println("Linha ignorada por colunas inválidas: " + lineNumber);
            continue;
          }

          // Corrige vírgulas nos campos decimais (índices 4 e 5)
          columns[4] = columns[4].replace(",", ".");
          columns[5] = columns[5].replace(",", ".");

          writer.write(String.join(";", columns));
          writer.newLine();
        }
      }

      // Agora faz o COPY do arquivo limpo
      Connection conn = DriverManager.getConnection(
              "jdbc:postgresql://localhost:5432/demoContabeis", "admin", "123456"
      );
      CopyManager copyManager = new CopyManager((BaseConnection) conn);
      FileReader fileReader = new FileReader(tempCsv.toFile());

      String copySQL = "COPY operadora_financeiro(data_registro, reg_ans, cd_conta_contabil, descricao, vl_saldo_inicial, vl_saldo_final) " +
              "FROM STDIN WITH (FORMAT csv, HEADER, DELIMITER ';', ENCODING 'UTF8')";

      long rowsAffected = copyManager.copyIn(copySQL, fileReader);
      System.out.println("Importados com sucesso: " + rowsAffected + " registros.");

    } catch (Exception e) {
      throw new RuntimeException("Erro ao importar CSV", e);
    }
  }
  }
