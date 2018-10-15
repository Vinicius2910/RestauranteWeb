package br.com.sistemaTCC.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Transacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lojista_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Lojista lojistaID;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Cliente clienteID;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date data;
	
	private int pontos;
	private int pontuacaoTotal;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}

	public Lojista getLojistaID() {
		return lojistaID;
	}
	public void setLojistaID(Lojista lojistaID) {
		this.lojistaID = lojistaID;
	}
	public Cliente getClienteID() {
		return clienteID;
	}
	public void setClienteID(Cliente clienteID) {
		this.clienteID = clienteID;
	}
	public int getPontos() {
		return pontos;
	}
	public void setPontos(int pontos) {
		this.pontos = pontos;
	}
	public int getPontuacaoTotal() {
		return pontuacaoTotal;
	}
	public void setPontuacaoTotal(int pontuacaoTotal) {
		this.pontuacaoTotal = pontuacaoTotal;
	}
	
}
