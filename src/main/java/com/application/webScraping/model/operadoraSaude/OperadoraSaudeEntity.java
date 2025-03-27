package com.application.webScraping.model.operadoraSaude;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.sql.Date;

@Entity
@Table(name = "operadoras_saude")
public class OperadoraSaudeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "registro_ans")
    private String regAns;
    @NotNull
    private String cnpj;
    @Column(name = "razao_social")
    private String razaoSocial;
    @Column(name = "nome_fantasia")
    private String nomeFantasia;
    private String modalidade;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String ddd;
    private String telefone;
    private String fax;
    @Column(name = "endereco_eletronico")
    private String endEletronico;
    private String representante;
    private String cargoRepresentante;
    @Column(name = "regiao_comercializacao")
    private Integer regiaoComercializacao;
    private Date dataRegAns;

    public OperadoraSaudeEntity() {
    }

    public OperadoraSaudeEntity(Long id, String regAns, String cnpj, String razaoSocial, String nomeFantasia, String modalidade, String logradouro, String numero, String complemento, String bairro, String cidade, String uf, String cep, String ddd, String telefone, String fax, String endEletronico, String representante, String cargoRepresentante, Integer regiaoComercializacao, Date dataRegAns) {
        this.id = id;
        this.regAns = regAns;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.modalidade = modalidade;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
        this.ddd = ddd;
        this.telefone = telefone;
        this.fax = fax;
        this.endEletronico = endEletronico;
        this.representante = representante;
        this.cargoRepresentante = cargoRepresentante;
        if(regiaoComercializacao == null) {

        }
        this.regiaoComercializacao = regiaoComercializacao;
        this.dataRegAns = dataRegAns;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegAns() {
        return regAns;
    }

    public void setRegAns(String regAns) {
        this.regAns = regAns;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEndEletronico() {
        return endEletronico;
    }

    public void setEndEletronico(String endEletronico) {
        this.endEletronico = endEletronico;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getCargoRepresentante() {
        return cargoRepresentante;
    }

    public void setCargoRepresentante(String cargoRepresentante) {
        this.cargoRepresentante = cargoRepresentante;
    }

    public Integer getRegiaoComercializacao() {
        return regiaoComercializacao;
    }

    public void setRegiaoComercializacao(Integer regiaoComercializacao) {
        this.regiaoComercializacao = regiaoComercializacao;
    }

    public Date getDataRegAns() {
        return dataRegAns;
    }

    public void setDataRegAns(Date dataRegAns) {
        this.dataRegAns = dataRegAns;
    }
}
