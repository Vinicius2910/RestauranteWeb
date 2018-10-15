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
public class Parametro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lojista_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
	private Lojista lojistaID;
	
	
	@NotNull()
	private int tipo;
	
	private double valor;
	
	public Lojista getLojistaID() {
		return lojistaID;
	}

	public void setLojistaID(Lojista lojistaID) {
		this.lojistaID = lojistaID;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
}
