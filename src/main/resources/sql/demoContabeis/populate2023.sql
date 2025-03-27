COPY operadora_financeiro(data_registro, reg_ans, cd_conta_contabil, descricao, vl_saldo_inicial, vl_saldo_final)
    FROM '/home/yanonjava/IdeaProjects/NivelamentoIntuitiveCare/webScraping/src/documents/Demonstrações Contabeis 2023/2023/1T2023.csv'
    WITH CSV HEADER DELIMITER ';' ENCODING 'UTF8';
COPY operadora_financeiro(data_registro, reg_ans, cd_conta_contabil, descricao, vl_saldo_inicial, vl_saldo_final)
    FROM '/home/yanonjava/IdeaProjects/NivelamentoIntuitiveCare/webScraping/src/documents/Demonstrações Contabeis 2023/2023/2t2023.csv'
    WITH CSV HEADER DELIMITER ';' ENCODING 'UTF8';
COPY operadora_financeiro(data_registro, reg_ans, cd_conta_contabil, descricao, vl_saldo_inicial, vl_saldo_final)
    FROM '/home/yanonjava/IdeaProjects/NivelamentoIntuitiveCare/webScraping/src/documents/Demonstrações Contabeis 2023/2023/3T2023.csv'
    WITH CSV HEADER DELIMITER ';' ENCODING 'UTF8';
COPY operadora_financeiro(data_registro, reg_ans, cd_conta_contabil, descricao, vl_saldo_inicial, vl_saldo_final)
    FROM '/home/yanonjava/IdeaProjects/NivelamentoIntuitiveCare/webScraping/src/documents/Demonstrações Contabeis 2023/2023/4T2023.csv'
    WITH CSV HEADER DELIMITER ';' ENCODING 'UTF8';