package com.application.webScraping.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OperadorFinanceiroDto {
  @CsvBindByName(column = "DATA")
  @CsvDate(value = "yyyy-MM-dd")
  private LocalDate dataRegistro;
  @CsvBindByName(column = "REG_ANS")
  private int regAns;
  @CsvBindByName(column = "CD_CONTA_CONTABIL")
  private long cdContaContabil;
  @CsvBindByName(column = "DESCRICAO")
  private String descricao;
  @CsvBindByName(column = "VL_SALDO_INICIAL")
  private BigDecimal vlSaldoInicial;
  @CsvBindByName(column = "VL_SALDO_FINAL")
  private BigDecimal vlSaldoFinal;
  public OperadorFinanceiroDto() {

  }

  public LocalDate getDataRegistro() {
    return dataRegistro;
  }

  public void setDataRegistro(LocalDate dataRegistro) {
    this.dataRegistro = dataRegistro;
  }

  public int getRegAns() {
    return regAns;
  }

  public void setRegAns(int regAns) {
    this.regAns = regAns;
  }

  public long getCdContaContabil() {
    return cdContaContabil;
  }

  public void setCdContaContabil(long cdContaContabil) {
    this.cdContaContabil = cdContaContabil;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public BigDecimal getVlSaldoInicial() {
    return vlSaldoInicial;
  }

  public void setVlSaldoInicial(BigDecimal vlSaldoInicial) {
    this.vlSaldoInicial = vlSaldoInicial;
  }

  public BigDecimal getVlSaldoFinal() {
    return vlSaldoFinal;
  }

  public void setVlSaldoFinal(BigDecimal vlSaldoFinal) {
    this.vlSaldoFinal = vlSaldoFinal;
  }
}
