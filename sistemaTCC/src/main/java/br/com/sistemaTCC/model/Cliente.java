package br.com.sistemaTCC.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cliente {

	@Id
	private Long cpf;
	
	//@NotNull(message = "NOME é obrigatória")
	private String nome;
	
	//@NotNull(message = "TELEFONE é obrigatória")
	private String telefone;
	
	//@NotNull(message = "Pontuação é obrigatória")
	private int pontuacao;

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}
	
}
