package br.com.sistemaTCC.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemaTCC.model.Premio;

public interface Premios extends JpaRepository<Premio, Long>  {
	
	public Premio findByNomeContaining(String nome);
	

}
