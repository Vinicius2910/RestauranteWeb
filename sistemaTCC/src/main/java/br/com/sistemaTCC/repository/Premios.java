package br.com.sistemaTCC.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemaTCC.model.Premio;

public interface Premios extends JpaRepository<Premio, Long>  {
	
	public Premio findByNomeContaining(String nome);
	

}
