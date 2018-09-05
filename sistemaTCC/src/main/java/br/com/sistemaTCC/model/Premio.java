package br.com.sistemaTCC.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Premio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@NotNull(message = "NOME é obrigatória")
	private String nome;
	
	@NotNull(message = "Quantidade é obrigatória")
	private int qtdPontos;
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getQtdPontos() {
		return qtdPontos;
	}

	public void setQtdPontos(int qtdPontos) {
		this.qtdPontos = qtdPontos;
	}

	
}
