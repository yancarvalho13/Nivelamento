package com.application.webScraping.model;


import com.application.webScraping.dto.OperadorFinanceiroDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name = "operadora_financeiro")
@IdClass(OperadoraFinanceiroEntityId.class)
public class OperadoraFinanceiroEntity {
  @Id
  private LocalDate dataRegistro;
  @Id
  private int regAns;
  @Id
  private long cdContaContabil;

  private String descricao;
  private BigDecimal vlSaldoInicial;
  private BigDecimal vlSaldoFinal;

  public OperadoraFinanceiroEntity() {

  }

  public OperadoraFinanceiroEntity(LocalDate dataRegistro, int regAns, long cdContaContabil, String descricao, BigDecimal vlSaldoInicial, BigDecimal vlSaldoFinal) {
    this.dataRegistro = dataRegistro;
    this.regAns = regAns;
    this.cdContaContabil = cdContaContabil;
    this.descricao = descricao;
    this.vlSaldoInicial = vlSaldoInicial;
    this.vlSaldoFinal = vlSaldoFinal;
  }

  public OperadoraFinanceiroEntity(OperadorFinanceiroDto dto) {
    this.dataRegistro = dto.dataRegistro();
    this.regAns = dto.regAns();
    this.cdContaContabil = dto.cdContaContabil();
    this.descricao = dto.descricao();
    this.vlSaldoInicial = dto.vlSaldoInicial();
    this.vlSaldoFinal = dto.vlSaldoFinal();
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
