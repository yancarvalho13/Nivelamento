package com.application.webScraping.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OperadorFinanceiroDto(LocalDate dataRegistro,
        int regAns,
        long cdContaContabil,
        String descricao,
        BigDecimal vlSaldoInicial,
        BigDecimal vlSaldoFinal) {
}
