COPY operadora_financeiro(data_registro, reg_ans, cd_conta_contabil, descricao, vl_saldo_inicial, vl_saldo_final)
    FROM 'src/documents/Demonstrações Contabeis 2024/2024/1T2024.csv'
    WITH CSV HEADER DELIMITER ';' ENCODING 'UTF8';
COPY operadora_financeiro(data_registro, reg_ans, cd_conta_contabil, descricao, vl_saldo_inicial, vl_saldo_final)
    FROM 'src/documents/Demonstrações Contabeis 2024/2024/2T2024.csv'
    WITH CSV HEADER DELIMITER ';' ENCODING 'UTF8';
COPY operadora_financeiro(data_registro, reg_ans, cd_conta_contabil, descricao, vl_saldo_inicial, vl_saldo_final)
    FROM 'src/documents/Demonstrações Contabeis 2024/2024/3T2024.csv'
    WITH CSV HEADER DELIMITER ';' ENCODING 'UTF8';
COPY operadora_financeiro(data_registro, reg_ans, cd_conta_contabil, descricao, vl_saldo_inicial, vl_saldo_final)
    FROM 'src/documents/Demonstrações Contabeis 2024/2024/4T2024.csv'
    WITH CSV HEADER DELIMITER ';' ENCODING 'UTF8';