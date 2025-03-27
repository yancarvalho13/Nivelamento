package com.application.webScraping.service.operadoraSaude;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;

@Service
public class OperadoraSaudeService {


  public void extractCsvData(String csvPath) {
    int expectedColumns = 20;

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

      String copySQL = "COPY operadoras_saude(registro_ans, cnpj, razao_social,nome_fantasia, modalidade , logradouro , numero , complemento , bairro , cidade , uf , cep , ddd , telefone , fax , endereco_eletronico , representante , cargo_representante,regiao_comercializacao , data_registro_ans )" +
      "FROM STDIN WITH (FORMAT csv, HEADER, DELIMITER ';', ENCODING 'UTF8')";

      long rowsAffected = copyManager.copyIn(copySQL, fileReader);
      System.out.println("Importados com sucesso: " + rowsAffected + " registros.");

    } catch (Exception e) {
      throw new RuntimeException("Erro ao importar CSV", e);
    }
  }
}
