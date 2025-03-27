package com.application.webScraping.model.operadoraFinanceira;


import com.application.webScraping.dto.OperadorFinanceiroDto;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name = "operadora_financeiro")
@IdClass(OperadoraFinanceiroEntityId.class)
public class OperadoraFinanceiroEntity {
  @Id
  @Column(name = "data_registro", nullable = false)
  private LocalDate dataRegistro;

  @Id
  @Column(name = "reg_ans", nullable = false)
  private int regAns;

  @Id
  @Column(name = "cd_conta_contabil", nullable = false)
  private long cdContaContabil;

  @Column(name = "descricao")
  private String descricao;

  @Column(name = "vl_saldo_inicial", nullable = false)
  private BigDecimal vlSaldoInicial = BigDecimal.ZERO;

  @Column(name = "vl_saldo_final", nullable = false)
  private BigDecimal vlSaldoFinal = BigDecimal.ZERO;

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
    this.dataRegistro = dto.getDataRegistro();
    this.regAns = dto.getRegAns();
    this.cdContaContabil = dto.getCdContaContabil();
    this.descricao = dto.getDescricao();
    this.vlSaldoInicial = dto.getVlSaldoInicial();
    this.vlSaldoFinal = dto.getVlSaldoFinal();
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
