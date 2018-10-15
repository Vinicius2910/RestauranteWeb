package br.com.sistemaTCC.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Premio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lojista_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Lojista lojistaID;
	
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

	public Lojista getLojistaID() {
		return lojistaID;
	}

	public void setLojistaID(Lojista lojistaID) {
		this.lojistaID = lojistaID;
	}

}
