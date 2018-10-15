package br.com.sistemaTCC.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Lojista {
	
	@Id
	private Long cpfCNPJ;
	
	@NotNull(message = "NOME é obrigatória")
	private String nome;
	
	@NotNull(message = "EMAIL é obrigatória")
	private String email;
	
	@NotNull(message = "SENHA é obrigatória")
	private String senha;

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getCpfCNPJ() {
		return cpfCNPJ;
	}
	public void setCpfCNPJ(Long cpfCNPJ) {
		this.cpfCNPJ = cpfCNPJ;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	 
}
