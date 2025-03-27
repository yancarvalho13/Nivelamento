SELECT
    os.registro_ans,
    os.razao_social,
    SUM(ofin.vl_saldo_final - ofin.vl_saldo_inicial) AS despesa_total
FROM operadora_financeiro ofin
         JOIN operadoras_saude os ON ofin.reg_ans = CAST(os.registro_ans AS INT)
WHERE
    (ofin.descricao ILIKE '%sinistro%' OR ofin.descricao ILIKE '%evento%')
  AND ofin.descricao ILIKE '%medico hospitalar%'
  AND ofin.data_registro >= (CURRENT_DATE - INTERVAL '1 year')
  AND ofin.data_registro <= CURRENT_DATE
GROUP BY os.registro_ans, os.razao_social
ORDER BY despesa_total DESC
LIMIT 10;