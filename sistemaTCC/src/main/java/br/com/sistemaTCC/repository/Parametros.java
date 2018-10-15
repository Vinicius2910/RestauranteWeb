package br.com.sistemaTCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemaTCC.model.Parametro;
import br.com.sistemaTCC.model.Premio;

public interface Parametros extends JpaRepository<Parametro, Integer>{
	public Parametro findByLojistaID(Long lojistaID);
}
