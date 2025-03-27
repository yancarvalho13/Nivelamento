CREATE TABLE operadora_financeiro (
                                      data_registro DATE NOT NULL,
                                      reg_ans INT NOT NULL,
                                      cd_conta_contabil BIGINT NOT NULL,
                                      descricao VARCHAR(255) NOT NULL,
                                      vl_saldo_inicial DECIMAL(18,2) NOT NULL,
                                      vl_saldo_final DECIMAL(18,2) NOT NULL,
                                      PRIMARY KEY (data_registro, reg_ans, cd_conta_contabil)
);