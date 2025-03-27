SELECT
    os.registro_ans,
    os.razao_social,
    SUM(ofin.vl_saldo_final - ofin.vl_saldo_inicial) AS despesa_total
FROM operadora_financeiro ofin
         JOIN operadoras_saude os ON ofin.reg_ans = CAST(os.registro_ans AS INT)
WHERE TRIM(ofin.descricao) = 'Despesas com Eventos / Sinistros'
  AND ofin.data_registro >= date_trunc('quarter', CURRENT_DATE - INTERVAL '3 months')
  AND ofin.data_registro < date_trunc('quarter', CURRENT_DATE)
GROUP BY os.registro_ans, os.razao_social
ORDER BY despesa_total DESC
    LIMIT 10;