SELECT reg_ans, descricao, SUM(vl_saldo_final - vl_saldo_inicial) AS total_despesas
FROM operadora_financeiro
WHERE descricao = 'EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS DE ASSISTÊNCIA A SAÚDE MEDICO HOSPITALAR'
  AND data_registro >= CURRENT_DATE - INTERVAL '1 year'
GROUP BY reg_ans, descricao
ORDER BY total_despesas DESC
    LIMIT 10;
